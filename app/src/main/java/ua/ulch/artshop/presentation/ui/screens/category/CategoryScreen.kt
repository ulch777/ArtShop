package ua.ulch.artshop.presentation.ui.screens.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.CategoryModel
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.components.PostcardAsyncImage


@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onCategoryClick: (category: CategoryModel) -> Unit,
    onStateChanged: (UiState<String>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.isLoading) {
        true -> onStateChanged(UiState.Loading)
        false -> onStateChanged(UiState.Success)
    }
    uiState.errorMessage?.let {
        onStateChanged(UiState.Error(it))
        viewModel.resetErrorMessage()
    }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = viewModel::refresh,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CategoryGreed(
            categories = uiState.categories,
            onCategoryClick = onCategoryClick
        )
    }
}

@Composable
fun CategoryGreed(
    categories: List<CategoryModel>,
    onCategoryClick: (category: CategoryModel) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.small)),
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                onCategoryClicked = { cat -> onCategoryClick(cat) }
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryModel,
    onCategoryClicked: (category: CategoryModel) -> Unit,
) {
    Card {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.small))
                .height(200.dp)
                .clickable { onCategoryClicked(category) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PostcardAsyncImage(
                modifier = Modifier
                    .weight(85f, true)
                    .fillMaxSize(),
                size = 320,
                imageUrl = category.imageUrl,
                contentDescription = category.title
            )
            Text(
                modifier = Modifier
                    .weight(15f, true)
                    .padding(top = dimensionResource(id = R.dimen.very_small)),
                textAlign = TextAlign.Center,
                style = typography.titleMedium,
                text = category.name ?: ""
            )
        }
    }

}

@Preview
@Composable
fun CategoryCardPreview() {
    CategoryCard(
        category = CategoryModel(
            id = 0,
            "Some category",
            ""
        ),
        onCategoryClicked = {}
    )
}

@Preview
@Composable
fun CategoryGreedPreview() {
    CategoryGreed(
        categories = listOf(
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
            CategoryModel(id = 0, name = "Some category"),
        ),
        onCategoryClick = {}
    )
}
