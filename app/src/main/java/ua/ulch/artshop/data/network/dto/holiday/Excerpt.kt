package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class Excerpt(
    @SerializedName("rendered")
    val rendered: String? = null,
    @SerializedName("featured_media")
    val featuredMedia: Int? = null
)