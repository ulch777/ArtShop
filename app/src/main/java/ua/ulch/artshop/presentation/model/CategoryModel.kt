package ua.ulch.artshop.presentation.model

data class CategoryModel(
    var id: Int = 0,
    var name: String? = null,
    var title: String? = null,
    var imageUrl: String? = null,
    var orderNumber: Int = 0
)