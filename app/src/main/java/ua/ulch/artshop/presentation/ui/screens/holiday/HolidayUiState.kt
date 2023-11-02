package ua.ulch.artshop.presentation.ui.screens.holiday

import android.os.Parcelable
import ua.ulch.artshop.presentation.model.HolidayModel

data class HolidayUiState(
    val holiday: HolidayModel? = null,
    val imageDownloaded: Boolean? = null,
    val errorMessage: String? = null,
    val shareImageObject: Parcelable? = null
)
