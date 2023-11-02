package ua.ulch.artshop.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.MonthModel


@Composable
fun MonthList(
    months: List<MonthModel>,
    onMonthClicked: (month: MonthModel) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.small)),
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.very_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.very_small)),
    ) {
        items(months) { month ->
            MonthCard(
                modifier = Modifier,
                month = month,
                onMonthClicked = onMonthClicked
            )
        }
    }
}

@Composable
fun MonthCard(
    modifier: Modifier,
    month: MonthModel,
    onMonthClicked: (month: MonthModel) -> Unit,
) {
    Card {
        PostcardAsyncImage(
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.small))
                .height(200.dp)
                .clickable { onMonthClicked(month) }
                .fillMaxSize(),
            size = 320,
            imageUrl = month.imageUrl,
            contentDescription = month.name
        )
    }
}

@Preview
@Composable
fun MonthCardPreview() {
    MonthCard(
        modifier = Modifier,
        month = MonthModel(name = "Month", imageUrl = ""),
        onMonthClicked = {}
    )
}

@Preview
@Composable
fun MonthListPreview() {
    MonthList(
        months = listOf(
            MonthModel(name = "Month", imageUrl = ""),
            MonthModel(name = "Month", imageUrl = ""),
            MonthModel(name = "Month", imageUrl = ""),
            MonthModel(name = "Month", imageUrl = ""),
            MonthModel(name = "Month", imageUrl = ""),
            MonthModel(name = "Month", imageUrl = ""),
            MonthModel(name = "Month", imageUrl = ""),
            MonthModel(name = "Month", imageUrl = ""),

            ),
        onMonthClicked = {}
    )
}