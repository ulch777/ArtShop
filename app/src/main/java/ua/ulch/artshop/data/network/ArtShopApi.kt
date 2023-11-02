package ua.ulch.artshop.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.ulch.artshop.data.network.dto.category.CategoryDto
import ua.ulch.artshop.data.network.dto.holiday.HolidayDto
import ua.ulch.artshop.data.network.dto.media.MediaDto
import ua.ulch.artshop.data.network.dto.month.MonthDto
import ua.ulch.artshop.data.network.dto.postcard.PostcardDto
import javax.inject.Singleton

@Singleton
interface ArtShopApi {

    companion object {
        private const val API = "wp-json/wp/v2/"
        private const val CATEGORIES = "categories"
        private const val POSTS = "posts"
        private const val HOLIDAYS = "holiday"
        private const val MONTH = "month"
        private const val MEDIA = "media"

    }

    @GET("$API$CATEGORIES/")
    suspend fun getCategories(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int?,
        @Query("parent") parent: Int?
    ): List<CategoryDto>

    @GET("$API$POSTS/")
    suspend fun getPagedPosts(
        @Query("categories") categoryId: Int?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<PostcardDto>

    @GET("$API$MONTH/")
    suspend fun getMonths(
        @Query("per_page") perPage: Int,
    ): List<MonthDto>

    @GET("$API$HOLIDAYS/")
    suspend fun getHolidays(
        @Query("per_page") perPage: Int,
        @Query("month") monthId: Int?
    ): List<HolidayDto>

    @GET("$API$MEDIA/{id}")
    suspend fun getMediaLink(
        @Path("id") mediaLink: Int
    ): MediaDto
}