package ua.ulch.artshop.presentation.ui.screens.holiday

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.ui.components.PostcardAsyncImage
import ua.ulch.artshop.presentation.ui.components.ShareCard
import ua.ulch.artshop.domain.SocialNetworks
import ua.ulch.artshop.presentation.ui.common.UiState

@Composable
fun HolidayScreen(
    viewModel: HolidayViewModel,
    onMorePagesClicked: (categoryId: String?, title: String?) -> Unit,
    onStateChanged: (UiState<String>) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    uiState.errorMessage?.let {
        onStateChanged(UiState.Error(it))
        viewModel.resetErrorMessage()
    }
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
    HolidayColumn(
        holiday = uiState.holiday,
        onMorePagesClicked = onMorePagesClicked,
        greetingText = viewModel.greetingText,
        updateGreetingText = { input -> viewModel.updateGreetingText(input) },
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

@Composable
fun HolidayColumn(
    holiday: HolidayModel?,
    onMorePagesClicked: (categoryId: String?, title: String?) -> Unit,
    greetingText: String,
    updateGreetingText: (String) -> Unit,
    onDownloadClick: (url: String?) -> Unit,
    onShareClick: (
        shareType: SocialNetworks,
        imageUrl: String?,
        greetingText: String
    ) -> Unit

) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        PostcardAsyncImage(
            modifier = Modifier
                .weight(1f, true)
                .padding(dimensionResource(id = R.dimen.small))
                .fillMaxWidth(),
            size = 640,
            imageUrl = holiday?.imageUrl,
            contentDescription = ""
        )
        holiday?.description?.let {
            Text(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.small))
                    .fillMaxWidth(),
                text = it,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        if (!holiday?.category.isNullOrEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.very_large))
                    .clickable {
                        onMorePagesClicked(
                            holiday?.category,
                            holiday?.title
                        )
                    },
                text = stringResource(id = R.string.more_cards),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        ShareCard(
            modifier = Modifier,
            imageUrl = holiday?.imageUrl,
            greetingText = greetingText,
            updateGreetingText = { greetingText ->
                updateGreetingText(greetingText)
            },
            onDownloadClick = { url -> onDownloadClick(url) },
            onShareClick = { shareType, imageUrl, greetingText ->
                onShareClick(
                    shareType,
                    imageUrl,
                    greetingText
                )
            }

        )
    }
}

@Preview
@Composable
fun HolidayColumnPreview() {
    HolidayColumn(
        holiday = HolidayModel(id = 0, title = "SomeHoliday", imageUrl = ""),
        onMorePagesClicked = { _, _ -> },
        greetingText = "",
        updateGreetingText = {},
        onDownloadClick = { _ -> },
        onShareClick = { _, _, _ -> }
    )
}

