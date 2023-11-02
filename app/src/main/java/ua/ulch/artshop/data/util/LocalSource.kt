package ua.ulch.artshop.data.util

import android.content.Context
import ua.ulch.artshop.R
import ua.ulch.artshop.data.Constants.EN_LANG_ID
import ua.ulch.artshop.data.Constants.UK_LANG_ID
import ua.ulch.artshop.presentation.model.CategoryModel
import ua.ulch.artshop.presentation.model.LanguageModel
import ua.ulch.artshop.presentation.model.NavigationDrawerModel
import ua.ulch.artshop.presentation.ui.common.SubcategoryId
import ua.ulch.artshop.presentation.ui.common.SubcategoryIdEn
import ua.ulch.artshop.presentation.ui.navigation.CalendarList
import ua.ulch.artshop.presentation.ui.navigation.Categories
import ua.ulch.artshop.presentation.ui.navigation.PostcardList
import ua.ulch.artshop.presentation.ui.navigation.Settings
import ua.ulch.artshop.presentation.ui.navigation.Subcategory

object LocalSource {

    fun getDrawerList(context: Context?, lang: String): List<NavigationDrawerModel> {
        return when (lang) {
            UK_LANG_ID -> getDrawerListUk(context)
            EN_LANG_ID -> getDrawerListEn(context)
            else -> getDrawerListEn(context)
        }
    }

    private fun getDrawerListUk(context: Context?): List<NavigationDrawerModel> {
        return listOf(
            NavigationDrawerModel("Всі категорії", Categories.route),
            NavigationDrawerModel(
                "Обране",
                "${PostcardList.route}/${SubcategoryId.FAVOURITE_CATEGORY_ID.id}/" +
                        "Обране"
            ),
            NavigationDrawerModel(
                "День народження",
                "${Subcategory.route}/${SubcategoryId.BIRTHDAY_CATEGORY_ID.id}/" +
                        "День народження"
            ),
            NavigationDrawerModel("Календар свят", CalendarList.route),
            NavigationDrawerModel(
                "Свята",
                "${Subcategory.route}/${SubcategoryId.HOLIDAYS_CATEGORY_ID.id}/" +
                        "Свята"
            ),
            NavigationDrawerModel(
                "На кожен день",
                "${Subcategory.route}/${SubcategoryId.EVERYDAY_CATEGORY_ID.id}/" +
                        "На кожен день"
            ),
            NavigationDrawerModel(
                "Знаменні події",
                "${Subcategory.route}/${SubcategoryId.SIGNIFICANT_CATEGORY_ID.id}/" +
                        "Знаменні події"
            ),
            NavigationDrawerModel(
                "Професійні свята",
                "${Subcategory.route}/${SubcategoryId.PROFESSIONAL_CATEGORY_ID.id}/" +
                        "Професійні свята"
            ),
            NavigationDrawerModel(
                "Різне",
                "${Subcategory.route}/${SubcategoryId.DIFFERENT_CATEGORY_ID.id}/" +
                        "Різне"
            ),
            NavigationDrawerModel(context?.getString(R.string.nav_item_rate), ""),
            NavigationDrawerModel(context?.getString(R.string.nav_item_settings), Settings.route, R.drawable.ukraine),
            NavigationDrawerModel(context?.getString(R.string.nav_item_invite), "", R.drawable.plus),
            NavigationDrawerModel(context?.getString(R.string.nav_item_exit), "")
        )

    }

    private fun getDrawerListEn(context: Context?) = listOf(
        NavigationDrawerModel("All categories", Categories.route),
        NavigationDrawerModel(
            "Favorites",
            "${PostcardList.route}/${SubcategoryIdEn.FAVOURITE_CATEGORY_ID.id}/" +
                    "Favorites"
        ),
        NavigationDrawerModel("Holidays calendar", CalendarList.route),
        NavigationDrawerModel(
            "Happy birthday",
            "${Subcategory.route}/${SubcategoryIdEn.BIRTHDAY_CATEGORY_ID.id}/" +
                    "Happy birthday"
        ),
        NavigationDrawerModel(
            "Thank you",
            "${PostcardList.route}/${SubcategoryIdEn.THANK_YOU_CATEGORY_ID.id}/" +
                    "Thank you"
        ),
        NavigationDrawerModel(
            "Every day",
            "${Subcategory.route}/${SubcategoryIdEn.EVERYDAY_CATEGORY_ID.id}/" +
                    "Every day"
        ),
        NavigationDrawerModel(
            "Romantic & love",
            "${Subcategory.route}/${SubcategoryIdEn.ROMANTIC_CATEGORY_ID.id}/" +
                    "Romantic & love"
        ),
        NavigationDrawerModel(
            "QUOTES",
            "${Subcategory.route}/${SubcategoryIdEn.QUOTES_CATEGORY_ID.id}/" +
                    "QUOTES"
        ),
        NavigationDrawerModel(
            "Friendship",
            "${Subcategory.route}/${SubcategoryIdEn.FRIENDSHIP_CATEGORY_ID.id}/" +
                    "Friendship"
        ),
        NavigationDrawerModel(context?.getString(R.string.nav_item_rate), ""),
        NavigationDrawerModel(context?.getString(R.string.nav_item_settings), Settings.route, R.drawable.united_states),
        NavigationDrawerModel(context?.getString(R.string.nav_item_invite), "", R.drawable.plus),
        NavigationDrawerModel(context?.getString(R.string.nav_item_exit), "")
    )

    fun languageList(): List<LanguageModel> =
        listOf(
            LanguageModel(R.string.english, R.drawable.united_states, EN_LANG_ID),
            LanguageModel(R.string.ukrainian, R.drawable.ukraine, UK_LANG_ID),
        )

//    companion object {
        const val HOLIDAYS_CATEGORIES_ID = -10
        const val DIFFERENT_CATEGORIES_ID = -20
        const val SIGNIFICANT_CATEGORIES_ID = -30
        const val EVERYDAY_CATEGORIES_ID = -40
//    }

    val holidaysCategories: List<CategoryModel>
        get() {
            return listOf(
                CategoryModel(id = 42, name = "Різдво", title = "Різдво"),
                CategoryModel(id = 257, name = "Щедрий вечір", title = "Щедрий вечір"),
                CategoryModel(id = 9, name = "Водохреща", title = "Водохреща"),
                CategoryModel(id = 14, name = "День Св Валентина", title = "День Св Валентина"),
                CategoryModel(id = 33, name = "Масниця", title = "Масниця"),
                CategoryModel(id = 2, name = "8 Березня", title = "8 Березня"),
                CategoryModel(id = 140, name = "Вербна неділя", title = "Вербна неділя"),
                CategoryModel(id = 4, name = "Великдень", title = "Великдень"),
                CategoryModel(id = 159, name = "День Перемоги", title = "День Перемоги"),
                CategoryModel(id = 12, name = "День Матері", title = "День Матері"),
                CategoryModel(id = 46, name = "Трійця", title = "Трійця"),
                CategoryModel(
                    id = 172,
                    name = "День Конституції України",
                    title = "День Конституції України"
                ),
                CategoryModel(id = 171, name = "День Молоді", title = "День Молоді"),
                CategoryModel(
                    id = 13,
                    name = "День Незалежності України",
                    title = "День Незалежності України"
                ),
                CategoryModel(id = 11, name = "День Знань", title = "День Знань"),
                CategoryModel(
                    id = 10,
                    name = "День захисника України",
                    title = "День захисника України"
                ),
                CategoryModel(
                    id = 15,
                    name = "День Святого Миколая",
                    title = "День Святого Миколая"
                ),
            )
        }
    val differentCategories: List<CategoryModel>
        get() {
            return listOf(
                CategoryModel(id = 19, name = "Дякую", title = "Дякую"),
                CategoryModel(id = 7, name = "Вітаю, зі святом", title = "Вітаю, зі святом"),
                CategoryModel(id = 31, name = "Кохаю", title = "Кохаю"),
                CategoryModel(id = 47, name = "Цитати", title = "Цитати"),
                CategoryModel(id = 17, name = "Для настрою", title = "Для настрою"),
            )

        }
    val significantCategories: List<CategoryModel>
        get() {
            return listOf(
                CategoryModel(id = 5, name = "Весілля", title = "Весілля"),
                CategoryModel(id = 160, name = "Новонароджений", title = "Новонароджений"),
                CategoryModel(id = 43, name = "Річниця весілля", title = "Річниця весілля"),
            )
        }
    val everyDayCategories: List<CategoryModel>
        get() {
            return listOf(
                CategoryModel(
                    id = 18,
                    name = "Доброго ранку, дня, вечора",
                    title = "Доброго ранку, дня, вечора"
                ),
                CategoryModel(id = 34, name = "На добраніч", title = "На добраніч"),
            )
        }
}
