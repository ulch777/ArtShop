package ua.ulch.artshop.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.errorprone.annotations.Keep

@Keep
@Entity(tableName = "holidays")
data class HolidayEntity(
    @PrimaryKey
    @ColumnInfo(name = "id", defaultValue = "0")
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "image_url")
    var imageUrl: String? = null,
    @ColumnInfo(name = "media_link")
    val mediaLink: Int? = null,
    @ColumnInfo(name = "category")
    val category: String?,
    @ColumnInfo(name = "date")
    val date: String?,
    @ColumnInfo(name = "month_id")
    var monthId: Int? = null,
    @ColumnInfo(name = "description")
    var description: String? = null,
)
