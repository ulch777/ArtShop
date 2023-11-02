package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("rendered")
    val rendered: String? = null
)