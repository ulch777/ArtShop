package ua.ulch.artshop.data.storage.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "categories", indices = [Index(value = ["checked"])])
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id", defaultValue = "0")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "order_number", defaultValue = "0")
    val orderNumber: Int,
    @ColumnInfo("parent", defaultValue = "0")
    val parent: Int = 0,
    @ColumnInfo(name = "checked", defaultValue = "0")
    val checked: Boolean,
)