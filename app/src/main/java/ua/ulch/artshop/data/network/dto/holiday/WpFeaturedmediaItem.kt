package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class WpFeaturedmediaItem(
    @SerializedName("href")
    val href: String? = null,

    @SerializedName("embeddable")
    val isEmbeddable: Boolean = false
)