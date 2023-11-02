package ua.ulch.artshop.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ua.ulch.artshop.presentation.model.PostcardModel
import ua.ulch.artshop.presentation.ui.screens.postcard_list.PostcardViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostcardPager(
    viewModel: PostcardViewModel,
    modifier: Modifier,
    onEmptyList: () -> Unit
) {

    val postcardItems: LazyPagingItems<PostcardModel>? =
        viewModel.postcards?.collectAsLazyPagingItems()
    if (postcardItems?.itemCount == 0) onEmptyList()
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = uiState.currentPosition,
        initialPageOffsetFraction = 0f
    ) { postcardItems?.itemCount ?: 0 }

    HorizontalPager(
        modifier = Modifier,
        state = pagerState,
        pageSpacing = 0.dp,
        userScrollEnabled = true,
        reverseLayout = false,
        contentPadding = PaddingValues(0.dp),
        beyondBoundsPageCount = 0,
        pageSize = PageSize.Fill,
        flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
        key = null,
        pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
            Orientation.Horizontal
        ),
        pageContent = { pagerIndex ->
            postcardItems?.let {
                LaunchedEffect(Unit) {
                    snapshotFlow { pagerState.currentPage }.collect { currentPage ->
                        viewModel.updateCurrentImage(postcardItems[currentPage])
                    }
                }

                Postcard(
                    modifier = modifier,
                    postcard = postcardItems[pagerIndex],
                    inFavorite = uiState.isInFavourite(postcardItems[pagerIndex]?.id),
                    onFavouriteClick = { postcard ->
                        viewModel.onFavouriteClick(postcard)
                    }
                )

                postcardItems.apply {
                    when {
                        loadState.refresh is LoadState.Error -> {
                            val error = postcardItems.loadState.refresh as LoadState.Error

                            Box(modifier = modifier) {
                                PagerErrorMessage(
                                    modifier = Modifier.fillMaxSize(),
                                    message = error.error.localizedMessage!!,
                                    onClickRetry = { retry() })
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val error = postcardItems.loadState.append as LoadState.Error
                            Box(modifier = modifier) {
                                PagerErrorMessage(
                                    modifier = Modifier.fillMaxSize(),
                                    message = error.error.localizedMessage!!,
                                    onClickRetry = { retry() })
                            }
                        }
                    }
                }
            }
        }
    )
}
