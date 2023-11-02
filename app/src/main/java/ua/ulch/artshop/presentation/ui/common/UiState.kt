package ua.ulch.artshop.presentation.ui.common

sealed class UiState<out String>{
    data object Success : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Error(val message: String?) : UiState<String>()
}