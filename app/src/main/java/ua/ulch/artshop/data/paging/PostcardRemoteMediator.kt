package ua.ulch.artshop.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ua.ulch.artshop.data.network.ArtShopApi
import ua.ulch.artshop.data.repository.mappers.Mapper
import ua.ulch.artshop.data.storage.LocaleBase
import ua.ulch.artshop.data.storage.entities.PostcardEntity
import ua.ulch.artshop.data.storage.entities.PostcardRemoteKeys


@ExperimentalPagingApi
class PostcardRemoteMediator(
    private val categoryId: Int,
    private val api: ArtShopApi,
    private val database: LocaleBase
) : RemoteMediator<Int, PostcardEntity>() {

    private val postcardsDao = database.postcardsDao
    private val postcardRemoteKeysDao = database.postcardRemoteKeysDao
    private val mapper = Mapper()
    private var endOfPaginationReached = false

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostcardEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = api.getPagedPosts(
                categoryId = categoryId,
                page = currentPage,
                perPage = 10
            )
            endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postcardsDao.deleteByCategoryId(categoryId = categoryId)
                    postcardRemoteKeysDao.clearRemoteKeys()
                }
                val keys = response.map { postcard ->
                    PostcardRemoteKeys(
                        id = postcard.id,
                        prevKey = prevPage,
                        nextKey = nextPage
                    )
                }
                postcardRemoteKeysDao.insertAll(remoteKey = keys)
                val entities = mapper.toPostcardEntities(
                    categoryId = categoryId,
                    listDto = response
                )
                postcardsDao.putAll(
                    entity = entities
                )
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    return if (e.code() == 400)
                        MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                    else MediatorResult.Error(e)
                }

                else -> {
                    return MediatorResult.Error(e)
                }
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PostcardEntity>
    ): PostcardRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                postcardRemoteKeysDao.remoteKeysPostcardId(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PostcardEntity>
    ): PostcardRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { postcard ->
                postcardRemoteKeysDao.remoteKeysPostcardId(id = postcard.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PostcardEntity>
    ): PostcardRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { postcard ->
                postcardRemoteKeysDao.remoteKeysPostcardId(id = postcard.id)
            }
    }
}