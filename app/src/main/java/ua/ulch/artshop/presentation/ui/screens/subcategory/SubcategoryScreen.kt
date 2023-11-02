package ua.ulch.artshop.presentation.ui.screens.subcategory

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.CategoryModel
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.navigation.PostcardList
import ua.ulch.artshop.presentation.ui.navigation.navigateSingleTopTo

@Composable
fun SubcategoryScreen(
    viewModel: SubcategoryViewModel,
    navController: NavHostController,
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

    SubcategoryColumn(
        subcategories = uiState.subcategories,
        onSubcategoryClicked = { id, name ->
            navController
                .navigateSingleTopTo("${PostcardList.route}/${id}/${name}")
        }
    )
}

@Composable
fun SubcategoryColumn(
    subcategories: List<CategoryModel>,
    onSubcategoryClicked: (Int, String?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(subcategories) { subcategory ->
            SubcategoryRow(
                modifier = Modifier,
                subcategory = subcategory,
                onSubcategoryClicked = { category ->
                    onSubcategoryClicked(category.id, category.name)
                }
            )
        }
    }
}

@Composable
fun SubcategoryRow(
    modifier: Modifier,
    subcategory: CategoryModel,
    onSubcategoryClicked: (category: CategoryModel) -> Unit
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.small))
            .clickable { onSubcategoryClicked(subcategory) }
    ) {
        Row(
            modifier = modifier
                .height(40.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.circle),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(
                modifier = modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.small)),
                text = subcategory.name.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

        }
        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp
        )
    }

}

@Preview
@Composable
fun SubcategoryRowPreview() {
    SubcategoryRow(
        modifier = Modifier,
        subcategory = CategoryModel(
            0,
            "Subcategory",
            ""
        ),
        onSubcategoryClicked = {}
    )
}
