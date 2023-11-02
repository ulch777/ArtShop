package ua.ulch.artshop.presentation.ui.screens.subcategory

import ua.ulch.artshop.presentation.model.CategoryModel

data class SubcategoryUiState(
    val subcategories: List<CategoryModel> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
