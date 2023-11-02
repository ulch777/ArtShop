package ua.ulch.artshop.data.network.dto.month

import com.google.gson.annotations.SerializedName

data class ImageCategories(
    @SerializedName("category-image")
    val categoryImage: CategoryImage? = null
)