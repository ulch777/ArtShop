package ua.ulch.artshop.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ua.ulch.artshop.data.Constants.PATTERN
import ua.ulch.artshop.data.helper.LocaleHelper
import ua.ulch.artshop.data.network.ArtShopApi
import ua.ulch.artshop.data.network.dto.category.CategoryDto
import ua.ulch.artshop.data.paging.PostcardRemoteMediator
import ua.ulch.artshop.data.preference.AppPreference
import ua.ulch.artshop.data.repository.mappers.Mapper
import ua.ulch.artshop.data.storage.LocaleBase
import ua.ulch.artshop.data.storage.entities.CategoryEntity
import ua.ulch.artshop.data.storage.entities.HolidayEntity
import ua.ulch.artshop.data.util.LocalSource
import ua.ulch.artshop.presentation.model.CategoryModel
import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.model.LanguageModel
import ua.ulch.artshop.presentation.model.MonthModel
import ua.ulch.artshop.presentation.model.PostcardModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

private const val NETWORK_PAGE_SIZE = 10
//Shared
private const val LAUNCH_COUNT = "launch_count"
private const val DATE_FIRST_LAUNCH = "date_first_launch"
private const val DO_NOT_SHOW_AGAIN = "do not_show_again"

//Min number of days
private const val DAYS_UNTIL_PROMPT = 3

//Min number of launches
private const val LAUNCHES_UNTIL_PROMPT = 3

@Singleton
class ArtShopRepositoryImpl @Inject constructor(
    private val api: ArtShopApi,
    private val database: LocaleBase,
    private val localeHelper: LocaleHelper,
    private val appPreference: AppPreference
) : ArtShopRepository {

    private val categoryDao = database.categoriesDao
    private val postcardsDao = database.postcardsDao
    private val favoritesDao = database.favoritesDao
    private val monthsDao = database.monthsDao
    private val holidaysDao = database.holidaysDao
    private val mapper = Mapper()
    private val calendar = Calendar.getInstance()
    private val mutex = Mutex()

    override suspend fun getCategories(parent: Int?) {
        val list = mutableListOf<CategoryDto>()
        var page = 1
        while (page < 3) {
            list.addAll(categoryRequest(page))
            page++
        }
        val entities = mapper.toCategoryEntities(list)
        saveCategoriesToDb(entities)
    }
    override suspend fun refreshCategories(){
        categoryDao.deleteAll()
        getCategories(null)
    }

    private suspend fun categoryRequest(page: Int): List<CategoryDto> {
        return api.getCategories(
            perPage = 100,
            page = page,
            parent = null
        )
    }

    override fun observeCategories(): Flow<List<CategoryModel>> = categoryDao.observeChecked()
        .map { items ->
            mapper.toCategoryModels(items)
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPostcardStream(categoryId: Int): Flow<PagingData<PostcardModel>> {
        val pagingSourceFactory = {
            postcardsDao.observePagedByCategoryId(categoryId)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = 3 * NETWORK_PAGE_SIZE,
                initialLoadSize = 2 * NETWORK_PAGE_SIZE,
            ),
            remoteMediator = PostcardRemoteMediator(
                categoryId = categoryId,
                api = api,
                database = database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData ->
                pagingData
                    .filter { it.imageUrl != null }
                    .map { entity -> mapper.toPostcardUiModel(entity) }
            }
    }

    override fun getFavouriteStream(): Flow<PagingData<PostcardModel>> {
        val pagingSourceFactory = {
            favoritesDao.observePagedByLocale(localeHelper.getCurrentLocale())
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = 3 * NETWORK_PAGE_SIZE,
                initialLoadSize = 2 * NETWORK_PAGE_SIZE,
            ),

            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    mapper.toPostcardModel(entity)
                }
            }
    }

    override suspend fun resetData() {
        mutex.withLock {
            categoryDao.deleteAll()
            postcardsDao.deleteAll()
            monthsDao.deleteAll()
            holidaysDao.deleteAll()
        }
    }

    override suspend fun observeCurrentLocale(): Flow<String> {
        return localeHelper.observeCurrentLocale()
    }

    override fun setCurrentLocale(language: String) {
        localeHelper.setCurrentLocale(language)
    }

    override fun getCurrentLocale(): String {
        return localeHelper.getCurrentLocale()
    }

    override fun setDoNotAskRate() {
        appPreference.saveDataBoolean(DO_NOT_SHOW_AGAIN, true)
    }

    override fun checkAskRate(): Boolean {
        if (appPreference.loadDataBoolean(DO_NOT_SHOW_AGAIN)) {
            return false
        }
        val launchCount = appPreference.loadDataLong(LAUNCH_COUNT) + 1
        appPreference.saveDataLong(LAUNCH_COUNT, launchCount)
        var dateFirstLaunch = appPreference.loadDataLong(DATE_FIRST_LAUNCH)
        if (dateFirstLaunch == 0L) {
            dateFirstLaunch = System.currentTimeMillis()
            appPreference.saveDataLong(DATE_FIRST_LAUNCH, dateFirstLaunch)
        }
        // Wait at least n days before opening
        if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= dateFirstLaunch + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                return true
            }
        }
        return false
    }


    override suspend fun setFavourite(postcard: PostcardModel) {
        val ids = favoritesDao.readByLocale(localeHelper.getCurrentLocale()).map { it.id }
        if (postcard.id in ids) favoritesDao.deleteById(postcard.id)
        else favoritesDao.insert(
            mapper.toFavouriteEntity(
                postcard,
                localeHelper.getCurrentLocale()
            )
        )
    }

    override fun observeFavouriteIds(): Flow<List<Int>> =
        favoritesDao.observeByLocale(localeHelper.getCurrentLocale())
            .map { items -> items.map { it.id } }

    override suspend fun observeMonths(): Flow<List<MonthModel>> {
        val result = api.getMonths(
            perPage = 100,
        )
        val entities = mapper.toMonthEntities(result)
        monthsDao.putAll(entities)
        return monthsDao.observeAll()
            .map { items -> mapper.toMonthModels(items) }
    }

    override suspend fun getHolidays(monthId: Int) {
        val result = api.getHolidays(
            perPage = 100,
            monthId = monthId
        )
        val entities = mapper.toHolidayEntities(monthId, result)
        val oldEntities = holidaysDao.readByMonthId(monthId)
            .filter { it.imageUrl != null }
        if (oldEntities.isEmpty()) {
            holidaysDao.putAll(entities)
            entities
                .sortedBy { it.date }
                .forEach { entity ->
                    setImageLink(entity)
                    holidaysDao.put(entity)
                }
        } else {
            val newEntitiesIds = entities.map { it.id }.sortedBy { it }
            val oldEntitiesIds = oldEntities.map { it.id }.sortedBy { it }

            if (!listsEqual(newEntitiesIds, oldEntitiesIds)) {
                val needBeAddedIds = newEntitiesIds.minus(oldEntitiesIds.toSet())
                val needBeDeletedIds = oldEntitiesIds.minus(newEntitiesIds.toSet())
                if (needBeAddedIds.isNotEmpty()) {
                    val needBeAdded =
                        needBeAddedIds.mapNotNull { id -> entities.find { it.id == id } }
                    holidaysDao.putAll(needBeAdded)
                    needBeAdded
                        .sortedBy { it.date }
                        .forEach { entity ->
                            setImageLink(entity)
                            holidaysDao.put(entity)
                        }
                }
                if (needBeDeletedIds.isNotEmpty()) {
                    needBeDeletedIds.forEach { id -> holidaysDao.deleteById(id) }
                }
            }
        }
    }

    override suspend fun observeTodayHolidays(): Flow<List<HolidayModel>> {
        return flow {
            val monthIndex = calendar[Calendar.MONTH]
            val dayIndex = calendar[Calendar.DAY_OF_MONTH]
            val month = monthsDao.getMonthByIndex(monthIndex)
            val df = SimpleDateFormat(PATTERN, Locale.getDefault())
            val result = api.getHolidays(
                perPage = 100,
                monthId = month.id
            )
            val filteredList = result.filter { dto ->
                val date = if (dto.toolsetMeta?.holidayData != null)
                    dto.toolsetMeta.holidayData.holidayData?.formatted?.let { df.parse(it) }
                else dto.toolsetMeta?.holidayDay?.holidayData?.formatted?.let { df.parse(it) }
                if (date != null) {
                    calendar.time = date
                }
                val currentDay = calendar[Calendar.DAY_OF_MONTH]
                currentDay == dayIndex
            }
            calendar.time = Date()
            val entities = mapper.toHolidayEntities(month.id, filteredList)

            entities.forEach { entity ->
                setImageLink(entity)
                holidaysDao.put(entity)
            }
            emit(mapper.toHolidayModels(entities))
        }

    }


    override suspend fun observeHolidays(monthId: Int): Flow<List<HolidayModel>> {
        return holidaysDao.observeByMonth(monthId)
            .map { items ->
                mapper.toHolidayModels(items)
            }
    }

    override suspend fun getHoliday(id: Int): Flow<HolidayModel> =
        holidaysDao.getHolidayById(id)
            .map { item -> mapper.toHolidayModel(item) }

    override suspend fun getSubcategories(parent: Int) {
        if (parent > 0) {
            getCategories(parent)
        }
    }

    override fun observeSubcategories(parent: Int): Flow<List<CategoryModel>> =
        when (parent) {
            LocalSource.HOLIDAYS_CATEGORIES_ID -> flowOf(LocalSource.holidaysCategories)
            LocalSource.DIFFERENT_CATEGORIES_ID -> flowOf(LocalSource.differentCategories)
            LocalSource.SIGNIFICANT_CATEGORIES_ID -> flowOf(LocalSource.significantCategories)
            LocalSource.EVERYDAY_CATEGORIES_ID -> flowOf(LocalSource.everyDayCategories)

            else -> {
                categoryDao.observeByParent(parent).map { items -> mapper.toCategoryModels(items) }
            }
        }

    override fun getLanguageList(): List<LanguageModel> = LocalSource.languageList()

    private suspend fun saveCategoriesToDb(newList: List<CategoryEntity>) {
        val oldList = categoryDao.readAll()
        if (newList.size < oldList.size) {
            categoryDao.deleteAll()
        }
        categoryDao.putAll(newList)
    }

    private suspend fun setImageLink(entity: HolidayEntity): HolidayEntity {
        val mediaLink = entity.mediaLink
        mediaLink?.let {
            val result = api.getMediaLink(mediaLink = mediaLink)
            entity.imageUrl = result.guid?.rendered
        }
        return entity
    }

    private fun listsEqual(list1: List<Int>, list2: List<Int>): Boolean {
        if (list1.size != list2.size) return false
        val pairList = list1.zip(list2)
        return pairList.all { (elt1, elt2) -> elt1 == elt2 }
    }
}