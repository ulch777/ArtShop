package ua.ulch.artshop.data.network.dto.category

import com.google.gson.annotations.SerializedName

data class TitleCategory(
    @SerializedName("raw")
    val raw: String? = null,

    @SerializedName("type")
    val type: String? = null
)