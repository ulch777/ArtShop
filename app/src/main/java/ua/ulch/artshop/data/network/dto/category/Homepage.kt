package ua.ulch.artshop.data.network.dto.category

import com.google.gson.annotations.SerializedName

data class Homepage(
    @SerializedName("checked")
    val checked: List<String>? = null,

    @SerializedName("type")
    val type: String? = null

)