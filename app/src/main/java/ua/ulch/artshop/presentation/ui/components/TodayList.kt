package ua.ulch.artshop.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.HolidayModel


@Composable
fun TodayList(
    holidays: List<HolidayModel>,
    onHolidayClicked: (holiday: HolidayModel) -> Unit,
) {
    if (holidays.isNotEmpty()) {
        Column {
            Text(
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.very_small))
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(id = R.string.today_holiday)
            )

            LazyRow(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.small)),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(holidays) { holiday ->
                    TodayCard(
                        modifier = Modifier,
                        holiday = holiday,
                        onHolidayClicked = {
                            onHolidayClicked(holiday)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TodayCard(
    modifier: Modifier,
    holiday: HolidayModel,
    onHolidayClicked: (holiday: HolidayModel) -> Unit,
) {
    Card {
        Column(
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.small))
                .width(240.dp)
                .height(160.dp)
                .clickable { onHolidayClicked(holiday) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier
                    .weight(15f, true)
                    .padding(bottom = dimensionResource(id = R.dimen.very_small)),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                text = holiday.title ?: ""
            )
            PostcardAsyncImage(
                modifier = modifier
                    .weight(85f, true)
                    .fillMaxSize(),
                size = 320,
                imageUrl = holiday.imageUrl,
                contentDescription = holiday.title
            )
        }
    }

}

@Preview
@Composable
fun TodayCardPreview() {
    TodayCard(
        modifier = Modifier,
        holiday = HolidayModel(title = "Title", imageUrl = ""),
        onHolidayClicked = { })
}

@Preview
@Composable
fun TodayListPreview() {
    TodayList(
        holidays = listOf(
            HolidayModel(title = "Title", imageUrl = ""),
            HolidayModel(title = "Title", imageUrl = ""),
            HolidayModel(title = "Title", imageUrl = ""),
            HolidayModel(title = "Title", imageUrl = ""),
            HolidayModel(title = "Title", imageUrl = ""),
            HolidayModel(title = "Title", imageUrl = ""),
        ),
        onHolidayClicked = {}
    )

}