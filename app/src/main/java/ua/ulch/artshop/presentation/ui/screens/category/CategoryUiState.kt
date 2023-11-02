package ua.ulch.artshop.presentation.ui.screens.category

import ua.ulch.artshop.presentation.model.CategoryModel

data class CategoryUiState(
    val categories: List<CategoryModel> = listOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)
