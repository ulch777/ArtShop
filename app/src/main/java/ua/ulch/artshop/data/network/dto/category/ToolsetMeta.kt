package ua.ulch.artshop.data.network.dto.category

import com.google.gson.annotations.SerializedName

data class ToolsetMeta(
    @SerializedName("image-categories")
    val imageCategories: ImageCategories? = null
)