package ua.ulch.artshop.presentation.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

private const val CATEGORIES_ROUTE = "categories"
private const val POSTCARD_LIST_ROUTE = "postcard_list"
private const val POSTCARD_PAGER_ROUTE = "postcard_pager"
private const val SUBCATEGORY_ROUTE = "subcategory"
private const val CALENDAR_ROUTE = "calendar"
private const val HOLIDAY_LIST_ROUTE = "holiday_list"
private const val HOLIDAY_ROUTE = "holiday"
private const val SETTINGS_ROUTE = "settings"


interface ArtShopDestinations {
    val route: String
}

object Categories : ArtShopDestinations {
    override val route = CATEGORIES_ROUTE
}

object PostcardList : ArtShopDestinations {
    override val route = POSTCARD_LIST_ROUTE
    private const val categoryTypeArg = CATEGORY_ID_KEY
    private const val titleTypeArg = TITLE_KEY
    val routeWithArgs = "$route/{$categoryTypeArg}/{$titleTypeArg}"
    val arguments = listOf(
        navArgument(categoryTypeArg) { type = NavType.IntType },
        navArgument(titleTypeArg) { type = NavType.StringType },
    )
}

object PostcardPager : ArtShopDestinations {
    override val route = POSTCARD_PAGER_ROUTE
    private const val titleTypeArg = TITLE_KEY
    val routeWithArgs = "$route/{$titleTypeArg}"
    val arguments = listOf(
        navArgument(titleTypeArg) { type = NavType.StringType },
    )
}

object Subcategory : ArtShopDestinations {
    override val route = SUBCATEGORY_ROUTE
    private const val subcategoryTypeArg = SUBCATEGORY_ID_KEY
    private const val titleTypeArg = TITLE_KEY
    val routeWithArgs = "$route/{$subcategoryTypeArg}/{$titleTypeArg}"
    val arguments = listOf(
        navArgument(subcategoryTypeArg) { type = NavType.IntType },
        navArgument(titleTypeArg) { type = NavType.StringType },
    )
}

object CalendarList : ArtShopDestinations {
    override val route = CALENDAR_ROUTE
}

object HolidayList : ArtShopDestinations {
    override val route = HOLIDAY_LIST_ROUTE
    private const val monthTypeArg = MONTH_ID_KEY
    private const val titleTypeArg = TITLE_KEY
    val routeWithArgs = "$route/{$monthTypeArg}/{$titleTypeArg}"
    val arguments = listOf(
        navArgument(monthTypeArg) { type = NavType.IntType },
        navArgument(titleTypeArg) { type = NavType.StringType },
    )
}

object Holiday : ArtShopDestinations {
    override val route = HOLIDAY_ROUTE
    private const val holidayTypeArg = HOLIDAY_ID_KEY
    private const val titleTypeArg = TITLE_KEY
    val routeWithArgs = "$route/{$holidayTypeArg}/{$titleTypeArg}"
    val arguments = listOf(
        navArgument(holidayTypeArg) { type = NavType.IntType },
        navArgument(titleTypeArg) { type = NavType.StringType },
    )
}

object Settings : ArtShopDestinations {
    override val route = SETTINGS_ROUTE
}