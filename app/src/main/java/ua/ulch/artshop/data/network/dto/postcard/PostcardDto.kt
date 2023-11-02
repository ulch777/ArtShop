package ua.ulch.artshop.data.network.dto.postcard

import com.google.gson.annotations.SerializedName

data class PostcardDto(
    @SerializedName("featured_media_src_url")
    val featuredMediaSrcUrl: String? = null,

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("link")
    val link: String? = null,

    @SerializedName("categories")
    val categories: List<Int>? = null,
)