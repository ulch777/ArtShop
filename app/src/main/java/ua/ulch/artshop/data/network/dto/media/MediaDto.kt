package ua.ulch.artshop.data.network.dto.media

import com.google.gson.annotations.SerializedName

data class MediaDto(
    @SerializedName("guid")
    val guid: Guid?
)
