package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class MoreCardsId(
    @SerializedName("raw")
    val raw: String? = null,

    @SerializedName("type")
    val type: String? = null
)