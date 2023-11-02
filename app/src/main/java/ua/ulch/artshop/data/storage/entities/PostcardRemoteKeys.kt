package ua.ulch.artshop.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "postcard_remote_keys")
data class PostcardRemoteKeys(
    @PrimaryKey
    @ColumnInfo(name = "id", defaultValue = "0")
    val id: Int = 0,
    val prevKey: Int?,
    val nextKey: Int?
)