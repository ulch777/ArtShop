package ua.ulch.artshop.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Keep
@Entity(tableName = "postcards")
data class PostcardEntity(
    @PrimaryKey
    @ColumnInfo(name = "id", defaultValue = "0")
    val id: Int = 0,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "web_url")
    val webUrl: String?,
    @ColumnInfo(name = "category_id")
    val categoryId: Int?,
    @ColumnInfo(name = "subcategory_id")
    val subcategoryId: Int?,
)