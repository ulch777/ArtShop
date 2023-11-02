package ua.ulch.artshop.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Keep
@Entity(tableName = "months")
data class MonthEntity(
    @PrimaryKey
    @ColumnInfo(name = "id", defaultValue = "0")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "index_number", defaultValue = "0")
    val indexNumber: Int = 0
)