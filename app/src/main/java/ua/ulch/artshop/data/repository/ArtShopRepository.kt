package ua.ulch.artshop.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ua.ulch.artshop.presentation.model.CategoryModel
import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.model.LanguageModel
import ua.ulch.artshop.presentation.model.MonthModel
import ua.ulch.artshop.presentation.model.PostcardModel

interface ArtShopRepository {

    suspend fun getCategories(parent: Int?)
    suspend fun refreshCategories()
    fun observeCategories(): Flow<List<CategoryModel>>

    fun getPostcardStream(categoryId: Int): Flow<PagingData<PostcardModel>>
    fun getFavouriteStream(): Flow<PagingData<PostcardModel>>

    suspend fun setFavourite(postcard: PostcardModel)
    fun observeFavouriteIds(): Flow<List<Int>>

    suspend fun observeMonths(): Flow<List<MonthModel>>
    suspend fun observeTodayHolidays(): Flow<List<HolidayModel>>

    suspend fun getHolidays(monthId: Int)
    suspend fun observeHolidays(monthId: Int): Flow<List<HolidayModel>>
    suspend fun getHoliday(id: Int): Flow<HolidayModel>

    suspend fun getSubcategories(parent: Int)
    fun observeSubcategories(parent: Int): Flow<List<CategoryModel>>

    suspend fun resetData()

    suspend fun observeCurrentLocale(): Flow<String>
    fun setCurrentLocale(language: String)
    fun getCurrentLocale(): String

    fun setDoNotAskRate()
    fun checkAskRate(): Boolean

    fun getLanguageList(): List<LanguageModel>
}