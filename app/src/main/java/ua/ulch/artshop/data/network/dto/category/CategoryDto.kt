package ua.ulch.artshop.data.network.dto.category


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryDto(
    @SerializedName("parent")
    val parent: Int = 0,

    @SerializedName("toolset-meta")
    val toolsetMeta: ToolsetMeta? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("id")
    val id: Int = 0,
)