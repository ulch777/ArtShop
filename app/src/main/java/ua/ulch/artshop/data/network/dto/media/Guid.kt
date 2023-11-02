package ua.ulch.artshop.data.network.dto.media

import com.google.gson.annotations.SerializedName

data class Guid(
    @SerializedName("rendered")
    val rendered: String? = null
)