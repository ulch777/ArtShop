package ua.ulch.artshop.presentation.ui.screens.holiday_list

import ua.ulch.artshop.presentation.model.HolidayModel

data class HolidayListUiState(
    val holidays: List<HolidayModel> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
