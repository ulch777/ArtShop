package ua.ulch.artshop.data.repository.mappers

import androidx.core.text.HtmlCompat
import ua.ulch.artshop.data.Constants.PATTERN
import ua.ulch.artshop.data.network.dto.category.CategoryDto
import ua.ulch.artshop.data.network.dto.holiday.HolidayDto
import ua.ulch.artshop.data.network.dto.month.MonthDto
import ua.ulch.artshop.data.network.dto.postcard.PostcardDto
import ua.ulch.artshop.data.storage.entities.CategoryEntity
import ua.ulch.artshop.data.storage.entities.FavoriteEntity
import ua.ulch.artshop.data.storage.entities.HolidayEntity
import ua.ulch.artshop.data.storage.entities.MonthEntity
import ua.ulch.artshop.data.storage.entities.PostcardEntity
import ua.ulch.artshop.presentation.model.CategoryModel
import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.model.MonthModel
import ua.ulch.artshop.presentation.model.PostcardModel
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Mapper {


    fun toCategoryEntities(listDto: List<CategoryDto>): List<CategoryEntity> {
        return listDto.map { item -> toCategoryEntity(item) }
    }

    fun toCategoryModels(entities: List<CategoryEntity>): List<CategoryModel> {
        return entities.map { item -> toCategoryModel(item) }
            .sortedBy { it.orderNumber }
    }

    fun toPostcardEntities(
        categoryId: Int,
        listDto: List<PostcardDto>
    ): List<PostcardEntity> {
        return listDto.map { item -> toPostcardEntity(categoryId, item) }
    }

    fun toFavouriteEntity(postcard: PostcardModel, locale: String): FavoriteEntity {
        return FavoriteEntity(
            id = postcard.id,
            imageUrl = postcard.imageUrl,
            webUrl = postcard.webUrl,
            locale = locale
        )
    }

    fun toMonthEntities(listDto: List<MonthDto>): List<MonthEntity> {
        return listDto.sortedBy { it.id }
            .mapIndexed { index, item -> toMonthEntity(index, item) }
    }

    fun toMonthModels(entities: List<MonthEntity>): List<MonthModel> {
        return entities.map { item -> toMonthModel(item) }
    }

    fun toHolidayModels(entities: List<HolidayEntity>): List<HolidayModel> {
        return entities.map { item -> toHolidayModel(item) }
    }

    fun toHolidayEntities(
        monthId: Int,
        listDto: List<HolidayDto>
    ): List<HolidayEntity> {
        return listDto.map { item -> toHolidayEntity(monthId, item) }
    }

    fun toPostcardUiModel(entity: PostcardEntity): PostcardModel {
        return PostcardModel(
            id = entity.id,
            imageUrl = entity.imageUrl,
            webUrl = entity.webUrl,
            isGif = isGif(entity.imageUrl)
        )
    }

    fun toPostcardModel(entity: FavoriteEntity): PostcardModel {
        return PostcardModel(
            id = entity.id,
            imageUrl = entity.imageUrl,
            webUrl = entity.webUrl,
            isGif = isGif(entity.imageUrl)
        )
    }

    private fun toCategoryEntity(categoryDto: CategoryDto): CategoryEntity {
        val image: String? = categoryDto.toolsetMeta?.imageCategories?.categoryImage?.raw
        val title: String? = categoryDto.toolsetMeta?.imageCategories?.titleCategory?.raw

        var checked = false

        if (categoryDto.toolsetMeta?.imageCategories?.homepage != null
            && categoryDto.toolsetMeta.imageCategories.homepage.checked?.isNotEmpty() == true
        )

            checked = categoryDto.toolsetMeta.imageCategories.homepage.checked[0] == "1"

        var orderNumber = -1

        if (categoryDto.toolsetMeta?.imageCategories?.orderNumber?.raw != null
            && categoryDto.toolsetMeta.imageCategories.orderNumber.raw.isNotEmpty()
        ) {
            orderNumber = categoryDto.toolsetMeta.imageCategories.orderNumber.raw.toInt()
        }
        if (orderNumber == -1) {
            checked = false
        }
        return CategoryEntity(
            id = categoryDto.id,
            name = categoryDto.name,
            title = title,
            image = image,
            orderNumber = orderNumber,
            parent = categoryDto.parent,
            checked = checked
        )
    }

    private fun toCategoryModel(entity: CategoryEntity): CategoryModel {
        return CategoryModel(
            id = entity.id,
            name = entity.name,
            title = entity.title,
            imageUrl = entity.image,
            orderNumber = entity.orderNumber
        )
    }

    private fun toPostcardEntity(
        categoryId: Int,
        postcardDto: PostcardDto
    ): PostcardEntity {
        return PostcardEntity(
            id = postcardDto.id,
            imageUrl = postcardDto.featuredMediaSrcUrl,
            webUrl = postcardDto.link,
            categoryId = categoryId,
            subcategoryId = postcardDto.categories?.get(0)
        )
    }

    private fun toMonthEntity(
        index: Int,
        monthDto: MonthDto
    ): MonthEntity {
        return MonthEntity(
            id = monthDto.id,
            name = monthDto.name,
            imageUrl = monthDto.toolsetMeta?.imageCategories?.categoryImage?.raw,
            indexNumber = index
        )
    }

    private fun toHolidayEntity(
        monthId: Int,
        holidayDto: HolidayDto
    ): HolidayEntity {
        val dateStr = if (holidayDto.toolsetMeta?.holidayData != null)
            holidayDto.toolsetMeta.holidayData.holidayData?.formatted
        else holidayDto.toolsetMeta?.holidayDay?.holidayData?.formatted

        return HolidayEntity(
            id = holidayDto.id,
            title = holidayDto.title?.rendered,
            date = dateStr,
            description = holidayDto.excerpt?.rendered,
            category = getHolidayCategory(holidayDto),
            monthId = monthId,
            mediaLink = holidayDto.featuredMedia,
            imageUrl = holidayDto.imageUrl
        )
    }

    private fun getHolidayCategory(holiday: HolidayDto): String? =
        holiday.toolsetMeta?.holidayData?.holidayData?.let {
            holiday.toolsetMeta.holidayData.moreCardsId?.raw
        }

    private fun toMonthModel(monthEntity: MonthEntity): MonthModel {
        return MonthModel(
            id = monthEntity.id,
            name = monthEntity.name,
            imageUrl = monthEntity.imageUrl
        )
    }

    internal fun toHolidayModel(holidayEntity: HolidayEntity): HolidayModel {
        val calendar = Calendar.getInstance()
        val df = SimpleDateFormat(PATTERN, Locale.getDefault())
        val date = holidayEntity.date
        val year = calendar[Calendar.YEAR]
        var dayOfWeek = ""
        var dayOfMonth = ""
        var month: String? = ""
        if (date != null) {
            val dateParse = df.parse(date)
            dateParse?.let { calendar.time = dateParse }
            dayOfWeek = getDayName(calendar[Calendar.DAY_OF_WEEK], Locale.getDefault())
            dayOfMonth = calendar[Calendar.DAY_OF_MONTH].toString()
            val currentMonth = calendar[Calendar.MONTH]
            month = getMonthName(currentMonth, Locale.getDefault())
        }
        val description = HtmlCompat.fromHtml(
            holidayEntity.description ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()

        return HolidayModel(
            id = holidayEntity.id,
            year = year.toString(),
            dayOfWeek = dayOfWeek,
            dayOfMonth = dayOfMonth,
            month = month,
            title = holidayEntity.title,
            description = description,
            category = holidayEntity.category,
            imageUrl = holidayEntity.imageUrl
        )
    }

    private fun getDayName(day: Int?, locale: Locale?): String {
        return day?.let {
            val symbols = DateFormatSymbols(locale)
            val dayNames: Array<String> = symbols.weekdays
            dayNames[day]
        }.orEmpty()
    }

    private fun getMonthName(month: Int?, locale: Locale?): String {
        return month?.let {
            val symbols = DateFormatSymbols(locale)
            val dayNames: Array<String> = symbols.months
            dayNames[month]
        }.orEmpty()
    }

    private fun isGif(imageUrl: String?): Boolean {
        val ext = imageUrl?.substring(imageUrl.length - 3)
        return ext.equals("gif", true)
    }
}