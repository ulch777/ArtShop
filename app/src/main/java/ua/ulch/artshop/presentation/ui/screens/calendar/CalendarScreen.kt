package ua.ulch.artshop.presentation.ui.screens.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.model.MonthModel
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.components.MonthList
import ua.ulch.artshop.presentation.ui.components.TodayList

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onHolidayClicked: (holiday: HolidayModel) -> Unit,
    onMonthClicked: (month: MonthModel) -> Unit,
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
    CalendarColumn(
        holidays = uiState.holidays,
        months = uiState.months,
        onHolidayClicked = onHolidayClicked,
        onMonthClicked = onMonthClicked
    )
}

@Composable
fun CalendarColumn(
    holidays: List<HolidayModel>,
    months: List<MonthModel>,
    onHolidayClicked: (holiday: HolidayModel) -> Unit,
    onMonthClicked: (month: MonthModel) -> Unit,
) {
    Column {
        TodayList(
            holidays = holidays,
            onHolidayClicked = { holiday ->
                onHolidayClicked(holiday)
            }
        )
        MonthList(
            months = months,
            onMonthClicked = onMonthClicked
        )
    }
}

@Preview
@Composable
fun CalendarColumnPreview() {
    CalendarColumn(
        holidays = listOf(
            HolidayModel(id = 0, title = "title", imageUrl = ""),
            HolidayModel(id = 0, title = "title", imageUrl = "")
        ),
        months = listOf(
            MonthModel(id = 0, name = "Month", imageUrl = ""),
            MonthModel(id = 0, name = "Month", imageUrl = ""),
            MonthModel(id = 0, name = "Month", imageUrl = ""),
            MonthModel(id = 0, name = "Month", imageUrl = ""),
            MonthModel(id = 0, name = "Month", imageUrl = ""),
            MonthModel(id = 0, name = "Month", imageUrl = ""),
        ),
        onHolidayClicked = {},
        onMonthClicked = {})
}

