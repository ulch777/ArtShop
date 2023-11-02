package ua.ulch.artshop.data.network.dto.month

import com.google.gson.annotations.SerializedName

data class MonthDto(
    @SerializedName("toolset-meta")
    val toolsetMeta: ToolsetMeta? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("id")
    val id: Int = 0
)