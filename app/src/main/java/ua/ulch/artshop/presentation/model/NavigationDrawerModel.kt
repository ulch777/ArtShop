package ua.ulch.artshop.presentation.model

data class NavigationDrawerModel(
    val label: String?,
    val route: String,
    val resId: Int? = null
)