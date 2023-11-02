package ua.ulch.artshop.presentation.ui.screens.postcard_list

import android.os.Parcelable
import ua.ulch.artshop.presentation.model.PostcardModel

data class PostcardUiState(
    val imageDownloaded: Boolean? = null,
    val currentPosition: Int = 0,
    val currentImage: PostcardModel? = null,
    val favouriteIds: List<Int> = listOf(),
    val errorMessage: String? = null,
    val shareImageObject: Parcelable? = null
) {
    fun isInFavourite(id: Int?): Boolean = id in favouriteIds

}



