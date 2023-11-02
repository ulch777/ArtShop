package ua.ulch.artshop.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.ulch.artshop.BuildConfig
import ua.ulch.artshop.R
import ua.ulch.artshop.data.helper.LocaleHelper
import ua.ulch.artshop.data.util.LocalSource
import ua.ulch.artshop.di.LocaleHelperEntryPoint
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.components.AdmobBanner
import ua.ulch.artshop.presentation.ui.components.AppBar
import ua.ulch.artshop.presentation.ui.components.DrawerContent
import ua.ulch.artshop.presentation.ui.components.ExitDialog
import ua.ulch.artshop.presentation.ui.components.InviteCard
import ua.ulch.artshop.presentation.ui.components.RateDialog
import ua.ulch.artshop.presentation.ui.components.ShowProgressBar
import ua.ulch.artshop.presentation.ui.navigation.ArtShopNavHost
import ua.ulch.artshop.presentation.ui.navigation.Categories
import ua.ulch.artshop.presentation.ui.navigation.navigateSingleTopTo
import ua.ulch.artshop.presentation.ui.theme.ArtShopTheme

private const val MARKET_URL = "market://details?id="

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val localeHelper: LocaleHelper = EntryPointAccessors.fromApplication(
            newBase, LocaleHelperEntryPoint::class.java
        ).localeHelper
        super.attachBaseContext(localeHelper.onAttach())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtShopTheme {
                ArtShopApp(onLocaleChanged = { onLocaleChanged() })
            }
        }
    }

    private fun onLocaleChanged() {
        recreate()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtShopApp(onLocaleChanged: () -> Unit) {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()
        var showSheet by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            topBar = {
                AppBar(title = uiState.title) {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            },
            drawerContent = {
                Surface {
                    DrawerContent(
                        LocalSource.getDrawerList(
                            context,
                            viewModel.getCurrentLocale()
                        )
                    ) { item ->
                        when (item.label) {
                            context.getString(R.string.nav_item_exit) -> viewModel.showExitDialog(
                                true
                            )

                            context.getString(R.string.nav_item_invite) -> {
                                showSheet = true
                            }

                            context.getString(R.string.nav_item_rate) -> goRating(context)

                            else -> navController.navigateSingleTopTo(item.route)
                        }
                        coroutineScope.launch {
                            delay(timeMillis = 250)
                            scaffoldState.drawerState.close()
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    AdmobBanner(modifier = Modifier.fillMaxWidth())
                }
            }
        ) { innerPadding ->
            Surface(modifier = Modifier.fillMaxSize()) {
                val showProgressBarState = remember { mutableStateOf(false) }
                uiState.state?.let {
                    LaunchedEffect(uiState.state) {
                        when (uiState.state) {
                            is UiState.Loading -> showProgressBarState.value = true
                            is UiState.Success -> showProgressBarState.value = false
                            is UiState.Error -> {
                                showProgressBarState.value = false
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = (uiState.state as UiState.Error).message
                                        ?: context.getString(R.string.unknown_error),
                                    duration = SnackbarDuration.Short
                                )
                                viewModel.resetErrorState()
                            }

                            else -> {}
                        }
                    }
                }

                BackHandler(true) {
                    if (scaffoldState.drawerState.isClosed) viewModel.showExitDialog(true)
                    else coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
                ArtShopNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    setTitle = { title -> viewModel.setTitle(title) },
                    onStateChanged = { state -> viewModel.changeState(state = state) }
                )
                if (showProgressBarState.value) {
                    ShowProgressBar()
                }
                if (showSheet) {
                    val modalBottomSheetState = rememberModalBottomSheetState()
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = modalBottomSheetState
                    ) {
                        InviteCard(
                            modifier = Modifier,
                            onInviteClick = { shareType ->
                                showSheet = false
                                viewModel.getInviteIntent(shareType)
                            }
                        )
                    }
                }
            }
        }
    }
    if (uiState.localeChanged) {
        navController.navigateSingleTopTo(Categories.route)
        onLocaleChanged()
        viewModel.resetLocaleChange()
    }
    if (uiState.showExitDialog) {
        ExitDialog(
            onDismissClick = { viewModel.showExitDialog(false) },
        )
    }
    if (uiState.showRateDialog) {
        RateDialog(
            onRateClick = {
                goRating(context = context)
                viewModel.setNeverRate()
            },
            onLaterClick = { viewModel.showRateDialog(false) },
            onDismissClick = { viewModel.setNeverRate() }
        )
    }
    uiState.invitationIntent?.let {
        context.startActivity(it)
        viewModel.resetInviteIntent()
    }
}

private fun goRating(context: Context) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(MARKET_URL + BuildConfig.APPLICATION_ID)
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


@Preview
@Composable
fun ArtShopAppPreview() {
    ArtShopTheme {
        ArtShopApp(onLocaleChanged = {})
    }
}







