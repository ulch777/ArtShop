package ua.ulch.artshop.data.network.dto.category

import com.google.gson.annotations.SerializedName

data class CategoryImage(
    @SerializedName("attachment_id")
    val attachmentId: Int = 0,

    @SerializedName("raw")
    val raw: String? = null,

    @SerializedName("type")
    val type: String? = null,
) {

}