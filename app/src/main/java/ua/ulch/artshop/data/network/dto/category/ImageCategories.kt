package ua.ulch.artshop.data.network.dto.category

import com.google.gson.annotations.SerializedName

data class ImageCategories(
    @SerializedName("category-image")
    val categoryImage: CategoryImage? = null,

    @SerializedName("title-category")
    val titleCategory: TitleCategory? = null,

    @SerializedName("order-number")
    val orderNumber: OrderNumber? = null,

    @SerializedName("homepage")
    val homepage: Homepage? = null,
)