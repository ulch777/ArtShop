package ua.ulch.artshop.presentation.ui.screens.postcard_list

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.ui.common.UiState
import ua.ulch.artshop.presentation.ui.components.PostcardPager
import ua.ulch.artshop.presentation.ui.components.ShareCard

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostcardPagerScreen(
    viewModel: PostcardViewModel,
    onStateChanged: (UiState<String>) -> Unit,
    onEmptyList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    uiState.errorMessage?.let {
        onStateChanged(UiState.Error(it))
        viewModel.resetErrorMessage()
    }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    uiState.imageDownloaded?.let {
        val message =
            if (it) stringResource(id = R.string.image_downloaded)
            else stringResource(id = R.string.image_not_downloaded)
        LaunchedEffect(Unit) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.resetIsDownloaded()
        }
    }
    uiState.shareImageObject?.let {
        when (it) {
            is Intent -> context.startActivity(it)
            is SharePhotoContent -> ShareDialog(context as Activity)
                .show(it, ShareDialog.Mode.AUTOMATIC)
        }
        viewModel.resetShareImageObject()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .clickable { keyboardController?.hide() },
        verticalArrangement = Arrangement.SpaceBetween

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true),
        ) {
            PostcardPager(
                modifier = Modifier,
                viewModel = viewModel,
                onEmptyList = onEmptyList
            )
        }

        Column(
            modifier = Modifier
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.small))
                    .fillMaxWidth()
                    .clickable {
                        viewModel.goWeb(
                            context,
                            url = uiState.currentImage?.webUrl
                        )
                    },
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.go_web),
                style = MaterialTheme.typography.titleMedium
            )
            ShareCard(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background),
                imageUrl = uiState.currentImage?.imageUrl,
                greetingText = viewModel.greetingText,
                updateGreetingText = { greetingText ->
                    viewModel.updateGreetingText(greetingText)
                },
                onDownloadClick = { url ->
                    viewModel.downloadImageFromUrl(url = url)
                },
                onShareClick = { shareType, imageUrl, greetingText ->
                    viewModel.shareImage(
                        shareType = shareType,
                        imageUrl = imageUrl,
                        greetingText = greetingText
                    )
                }
            )
        }

    }
}

