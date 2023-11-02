package ua.ulch.artshop.presentation.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import ua.ulch.artshop.BuildConfig
import ua.ulch.artshop.R
import ua.ulch.artshop.domain.SocialNetworks
import ua.ulch.artshop.presentation.ui.common.debounceClickable

@Composable
fun ShareCard(
    modifier: Modifier,
    imageUrl: String?,
    greetingText: String,
    updateGreetingText: (String) -> Unit,
    onDownloadClick: (url: String?) -> Unit,
    onShareClick: (
        shareType: SocialNetworks,
        imageUrl: String?,
        greetingText: String
    ) -> Unit
) {
    val context = LocalContext.current
    loadInterstitial(context)

    val disableShareButtons = remember { mutableStateOf(false) }

    val afterAdAction: (
        shareType: SocialNetworks,
        imageUrl: String?,
        greetingText: String
    ) -> Unit = { sType, imUrl, gText ->

        disableShareButtons.value = false
        onShareClick(sType, imUrl, gText)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = modifier
                .padding(vertical = dimensionResource(id = R.dimen.small))
                .fillMaxWidth(),
            value = greetingText,
            onValueChange = { updateGreetingText(it) },
            label = { Text(stringResource(id = R.string.greeting)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround) {
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        disableShareButtons.value = true
                        onDownloadClick(
                            context = context,
                            imageUrl = imageUrl,
                            afterAdAction = { url ->
                                disableShareButtons.value = false
                                onDownloadClick(url)
                            }
                        )
                    },
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        disableShareButtons.value = true
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.FACEBOOK,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "",
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.TELEGRAM,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_telegram),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.VIBER,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_viber),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.WHATSAPP,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_whatsapp),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.PINTEREST,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_pinterest),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.TWITTER,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_twitter),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.INSTAGRAM,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_instagramm),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.very_small))
                    .size(32.dp)
                    .debounceClickable {
                        onShareClicked(
                            context = context,
                            shareType = SocialNetworks.NOT_SPECIFIED,
                            imageUrl = imageUrl,
                            greetingText = greetingText,
                            afterAdAction = afterAdAction
                        )
                    },
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = ""
            )
        }

    }

}

var interstitialAd: InterstitialAd? = null

fun loadInterstitial(context: Context) {
    InterstitialAd.load(
        context,
        BuildConfig.INTERSTITIAL_AD_ID,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
                loadInterstitial(context)
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
            }
        }
    )
}

fun onDownloadClick(
    context: Context,
    imageUrl: String?,
    afterAdAction: (String?) -> Unit
) {
    val activity = context.findActivity()
    if (interstitialAd != null && activity != null) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(e: AdError) {
                interstitialAd = null
                loadInterstitial(context)
                afterAdAction(imageUrl)
            }

            override fun onAdShowedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(context)
            }

            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(context)
                afterAdAction(imageUrl)
            }
        }
        interstitialAd?.show(activity)
    }
}

// Show ad
fun onShareClicked(
    context: Context,
    shareType: SocialNetworks,
    imageUrl: String?,
    greetingText: String,
    afterAdAction: (SocialNetworks, String?, String) -> Unit
) {
    val activity = context.findActivity()

    if (interstitialAd != null && activity != null) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(e: AdError) {
                interstitialAd = null
                loadInterstitial(context)
                afterAdAction(shareType, imageUrl, greetingText)
            }

            override fun onAdShowedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(context)
            }

            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(context)
                afterAdAction(shareType, imageUrl, greetingText)
            }
        }
        interstitialAd?.show(activity)
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}