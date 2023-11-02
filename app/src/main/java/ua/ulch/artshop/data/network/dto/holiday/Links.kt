package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("wp:featuredmedia")
    val wpFeaturedmedia: List<WpFeaturedmediaItem>? = null
)