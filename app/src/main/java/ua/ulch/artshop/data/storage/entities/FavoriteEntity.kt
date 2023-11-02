package ua.ulch.artshop.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "web_url")
    val webUrl: String?,
    @ColumnInfo(name = "locale")
    val locale: String
)