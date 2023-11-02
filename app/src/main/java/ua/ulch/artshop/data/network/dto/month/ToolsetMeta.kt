package ua.ulch.artshop.data.network.dto.month

import com.google.gson.annotations.SerializedName

data class ToolsetMeta(
    @SerializedName("image-categories")
    val imageCategories: ImageCategories? = null
)