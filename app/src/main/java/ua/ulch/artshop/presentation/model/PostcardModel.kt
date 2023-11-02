package ua.ulch.artshop.presentation.model

data class PostcardModel(
    var id: Int = 0,
    var imageUrl: String?,
    var webUrl: String? = null,
    var isGif: Boolean = false,
)