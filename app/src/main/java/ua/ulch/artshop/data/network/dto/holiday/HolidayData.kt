package ua.ulch.artshop.data.network.dto.holiday

import com.google.gson.annotations.SerializedName

data class HolidayData(
    @SerializedName("holiday-data")
    val holidayData: HolidayData? = null,

    @SerializedName("more-cards-id")
    val moreCardsId: MoreCardsId? = null,

    @SerializedName("formatted")
    val formatted: String? = null,

    @SerializedName("raw")
    val raw: String? = null,

    @SerializedName("type")
    val type: String? = null
)