package ua.ulch.artshop.presentation.ui.screens.holiday_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.components.PostcardAsyncImage


@Composable
fun HolidayListScreen(
    viewModel: HolidayListViewModel,
    onHolidayClick: (holiday: HolidayModel) -> Unit,
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
    HolidayColumn(
        holidays = uiState.holidays,
        onHolidayClick = onHolidayClick
    )
}

@Composable
fun HolidayColumn(
    holidays: List<HolidayModel>,
    onHolidayClick: (holiday: HolidayModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.very_small))
    ) {
        items(holidays) { item ->
            HolidayCard(
                holiday = item,
                onHolidayClick = { holiday ->
                    onHolidayClick(holiday)
                }
            )
        }
    }
}

@Composable
fun HolidayCard(
    holiday: HolidayModel,
    onHolidayClick: (holiday: HolidayModel) -> Unit
) {
    Card(modifier = Modifier.height(120.dp)) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.very_small))
                .fillMaxWidth()
                .clickable { onHolidayClick(holiday) }
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.primary),
                        text = holiday.year.orEmpty(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,

                        )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = holiday.dayOfWeek.orEmpty(),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = holiday.dayOfMonth.orEmpty(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = holiday.month.orEmpty(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )

                }
            }
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(dimensionResource(id = R.dimen.very_small)),
                    text = holiday.title ?: "",
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(dimensionResource(id = R.dimen.very_small)),
                    text = holiday.description ?: "",
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodySmall
                )

            }
            PostcardAsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp)
                    .fillMaxSize(),
                size = 320,
                imageUrl = holiday.imageUrl,
                contentDescription = ""
            )
        }
    }

}

@Preview
@Composable
fun HolidayCardPreview() {
    HolidayCard(
        holiday = HolidayModel(
            id = 0,
            "2023",
            "Friday",
            "21",
            "December",
            "Some Holiday",
            "This is some holiday description. Every Friday is a little holiday",
            "",
            imageUrl = ""
        ),
        onHolidayClick = {}
    )
}

@Preview
@Composable
fun HolidayColumnPreview(){
    HolidayColumn(
        holidays = listOf(
            HolidayModel(id = 0, title = "Some holiday", imageUrl = ""),
            HolidayModel(id = 0, title = "Some holiday", imageUrl = ""),
            HolidayModel(id = 0, title = "Some holiday", imageUrl = ""),
            HolidayModel(id = 0, title = "Some holiday", imageUrl = ""),
            HolidayModel(id = 0, title = "Some holiday", imageUrl = ""),
            HolidayModel(id = 0, title = "Some holiday", imageUrl = ""),
        ),
        onHolidayClick = {}
    )
}
