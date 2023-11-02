package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class ToolsetMeta(
    @SerializedName("holiday-data")
    val holidayData: HolidayData? = null,
    @SerializedName("holiday-day")
    val holidayDay: HolidayData? = null
)