package ua.ulch.artshop.presentation.main

import android.content.Intent
import ua.ulch.artshop.presentation.ui.common.UiState

data class MainUiState(
    var title: String = "",
    val showRateDialog: Boolean = false,
    val showExitDialog: Boolean = false,
    val state: UiState<String>? = null,
    val localeChanged: Boolean = false,
    val invitationIntent: Intent? = null
)
