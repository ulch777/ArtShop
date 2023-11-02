package ua.ulch.artshop.presentation.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.ui.common.ScaleTransitionDirection
import ua.ulch.artshop.presentation.ui.common.SubcategoryId
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.common.scaleIntoContainer
import ua.ulch.artshop.presentation.ui.common.scaleOutOfContainer
import ua.ulch.artshop.presentation.ui.screens.calendar.CalendarScreen
import ua.ulch.artshop.presentation.ui.screens.calendar.CalendarViewModel
import ua.ulch.artshop.presentation.ui.screens.category.CategoryScreen
import ua.ulch.artshop.presentation.ui.screens.category.CategoryViewModel
import ua.ulch.artshop.presentation.ui.screens.holiday.HolidayScreen
import ua.ulch.artshop.presentation.ui.screens.holiday.HolidayViewModel
import ua.ulch.artshop.presentation.ui.screens.holiday_list.HolidayListScreen
import ua.ulch.artshop.presentation.ui.screens.holiday_list.HolidayListViewModel
import ua.ulch.artshop.presentation.ui.screens.postcard_list.PostcardListScreen
import ua.ulch.artshop.presentation.ui.screens.postcard_list.PostcardPagerScreen
import ua.ulch.artshop.presentation.ui.screens.postcard_list.PostcardViewModel
import ua.ulch.artshop.presentation.ui.screens.settings.SettingsScreen
import ua.ulch.artshop.presentation.ui.screens.settings.SettingsViewModel
import ua.ulch.artshop.presentation.ui.screens.subcategory.SubcategoryScreen
import ua.ulch.artshop.presentation.ui.screens.subcategory.SubcategoryViewModel

const val TITLE_KEY = "title"
const val CATEGORY_ID_KEY = "category_id"
const val SUBCATEGORY_ID_KEY = "subcategory_id"
const val MONTH_ID_KEY = "month_id"
const val HOLIDAY_ID_KEY = "holiday_id"
private const val CALENDAR_ID = 350

@Composable
fun ArtShopNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    setTitle: (String) -> Unit,
    onStateChanged: (UiState<String>) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Categories.route,
        modifier = modifier

    ) {
        composable(
            route = Categories.route
        ) {
            setTitle(stringResource(id = R.string.nav_item_categories))
            val viewModel = hiltViewModel<CategoryViewModel>()
            CategoryScreen(
                viewModel,
                onCategoryClick = { category ->
                    if (category.id != CALENDAR_ID) {
                        navController.navigateSingleTopTo(
                            "${PostcardList.route}/${category.id}/${category.name}"
                        )
                    } else {
                        navController.navigateSingleTopTo(CalendarList.route)
                    }
                },
                onStateChanged = onStateChanged
            )
        }
        composable(
            route = PostcardList.routeWithArgs,
            arguments = PostcardList.arguments,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString(TITLE_KEY)
            val categoryId = backStackEntry.arguments?.getInt(CATEGORY_ID_KEY)!!
            setTitle(title ?: stringResource(id = R.string.app_name))
            val viewModel = hiltViewModel<PostcardViewModel>()
            viewModel.getPostcards(categoryId)
            if (categoryId == SubcategoryId.FAVOURITE_CATEGORY_ID.id) {
                BackHandler(true) {
                    navController.popBackStack(Categories.route, false)
                }
            }
            PostcardListScreen(
                viewModel = viewModel,
                onStateChanged = onStateChanged,
                onPostcardClick = {
                    navController.navigateSingleTopTo("${PostcardPager.route}/${title}")
                }
            )
        }
        composable(
            route = PostcardPager.routeWithArgs,
            arguments = PostcardPager.arguments,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString(TITLE_KEY)
            setTitle(title ?: stringResource(id = R.string.app_name))

            val parentEntry: NavBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(PostcardList.routeWithArgs)
            }
            val viewModel = hiltViewModel<PostcardViewModel>(parentEntry)
            PostcardPagerScreen(
                viewModel = viewModel,
                onStateChanged = onStateChanged,
                onEmptyList = { navController.popBackStack(PostcardList.routeWithArgs, false) }
            )

        }

        composable(
            route = Subcategory.routeWithArgs,
            arguments = Subcategory.arguments,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString(TITLE_KEY)
            val subcategoryId = backStackEntry.arguments?.getInt(SUBCATEGORY_ID_KEY)
            setTitle(title ?: stringResource(id = R.string.app_name))
            val viewModel = hiltViewModel<SubcategoryViewModel>()
            viewModel.getSubcategories(subcategoryId)
            BackHandler(true) {
                navController.popBackStack(Categories.route, false)
            }
            SubcategoryScreen(
                viewModel = viewModel,
                navController = navController,
                onStateChanged = onStateChanged
            )
        }
        composable(
            route = CalendarList.route,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) {
            val title = stringResource(id = R.string.nav_item_calendar)
            setTitle(title)
            val viewModel = hiltViewModel<CalendarViewModel>()
            BackHandler(true) {
                navController.popBackStack(Categories.route, false)
            }
            CalendarScreen(
                viewModel = viewModel,
                onHolidayClicked = { holiday ->
                    navController.navigateSingleTopTo(
                        "${Holiday.route}/${holiday.id}/${holiday.title}"
                    )
                },
                onMonthClicked = { month ->
                    navController.navigateSingleTopTo(
                        "${HolidayList.route}/${month.id}/${month.name}"
                    )
                },
                onStateChanged = onStateChanged
            )

        }

        composable(
            route = HolidayList.routeWithArgs,
            arguments = HolidayList.arguments,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString(TITLE_KEY)
            setTitle(title ?: stringResource(id = R.string.app_name))
            val viewModel = hiltViewModel<HolidayListViewModel>()
            HolidayListScreen(
                viewModel = viewModel,
                onHolidayClick = { holiday ->
                    navController.navigateSingleTopTo(
                        "${Holiday.route}/${holiday.id}/${holiday.title}"
                    )
                },
                onStateChanged = onStateChanged
            )
        }
        composable(
            route = Holiday.routeWithArgs,
            arguments = Holiday.arguments,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString(TITLE_KEY)
            setTitle(title ?: stringResource(id = R.string.app_name))
            val viewModel = hiltViewModel<HolidayViewModel>()
            HolidayScreen(
                viewModel = viewModel,
                onMorePagesClicked = { categoryId, categoryTitle ->
                    navController
                        .navigateSingleTopTo(
                            "${PostcardList.route}/${categoryId}/${categoryTitle}"
                        )
                },
                onStateChanged = onStateChanged
            )
        }
        composable(
            route = Settings.route,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) {
            val title = stringResource(id = R.string.nav_item_settings)
            setTitle(title)
            val viewModel = hiltViewModel<SettingsViewModel>()

            SettingsScreen(
                viewModel = viewModel,
                onStateChanged = onStateChanged
            )

        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) { launchSingleTop = true }
}


