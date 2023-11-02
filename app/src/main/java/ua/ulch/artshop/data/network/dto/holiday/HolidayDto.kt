package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class HolidayDto(
    @SerializedName("date")
    val date: String?,

    @SerializedName("_links")
    val links: Links? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("title")
    val title: Title? = null,

    @SerializedName("featured_media")
    val featuredMedia: Int = 0,

    @SerializedName("toolset-meta")
    val toolsetMeta: ToolsetMeta? = null,

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("month")
    val month: List<Int>? = null,

    @SerializedName("excerpt")
    val excerpt: Excerpt? = null,

    var imageUrl: String? = null
)