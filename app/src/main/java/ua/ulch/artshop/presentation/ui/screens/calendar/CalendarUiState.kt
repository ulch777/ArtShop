package ua.ulch.artshop.presentation.ui.screens.calendar

import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.model.MonthModel

data class CalendarUiState(
    val months: List<MonthModel> = listOf(),
    val holidays: List<HolidayModel> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
