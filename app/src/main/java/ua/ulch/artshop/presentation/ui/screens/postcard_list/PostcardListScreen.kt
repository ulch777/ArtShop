package ua.ulch.artshop.presentation.ui.screens.postcard_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.PostcardModel
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.components.ErrorMessage
import ua.ulch.artshop.presentation.ui.components.LoadingNextPageItem
import ua.ulch.artshop.presentation.ui.components.PageLoader
import ua.ulch.artshop.presentation.ui.components.PostcardCardItem


@Composable
fun PostcardListScreen(
    viewModel: PostcardViewModel,
    onStateChanged: (UiState<String>) -> Unit,
    onPostcardClick: () -> Unit,
) {
    val postcardItems: LazyPagingItems<PostcardModel>? =
        viewModel.postcards?.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    uiState.errorMessage?.let {
        onStateChanged(UiState.Error(it))
        viewModel.resetErrorMessage()
    }
    PostcardGreed(
        postcardItems = postcardItems,
        onPostcardClick = { index ->
            viewModel.updateCurrentPosition(index)
            onPostcardClick()
        })
}

@Composable
fun PostcardGreed(
    postcardItems: LazyPagingItems<PostcardModel>?,
    onPostcardClick: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.small)),
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        items(
            count = postcardItems?.itemCount?: 0,
        ) { index ->

            postcardItems?.let{
                PostcardCardItem(
                    modifier = Modifier,
                    postcard = postcardItems[index],
                    onPostcardClicked = {
                        onPostcardClick(index)
                    }
                )
            }
        }
        postcardItems?.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) { PageLoader(modifier = Modifier.fillMaxSize()) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = postcardItems.loadState.refresh as LoadState.Error
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorMessage(
                            modifier = Modifier.fillMaxSize(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) { LoadingNextPageItem(modifier = Modifier.fillMaxSize()) }
                }

                loadState.append is LoadState.Error -> {
                    val error = postcardItems.loadState.append as LoadState.Error
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorMessage(
                            modifier = Modifier.fillMaxSize(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }
}
