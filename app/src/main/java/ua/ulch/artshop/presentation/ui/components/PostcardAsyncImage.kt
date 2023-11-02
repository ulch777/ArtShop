package ua.ulch.artshop.presentation.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Precision

@Composable
fun PostcardAsyncImage(
    modifier: Modifier,
    size: Int,
    imageUrl: String?,
    isGif: Boolean = false,
    contentDescription: String?,
) {
    if (!isGif) {
        JpgPostcardAsyncImage(
            modifier = modifier,
            size = size,
            imageUrl = imageUrl,
            contentDescription = contentDescription,
        )
    } else {
        GifPostcardAsyncImage(
            modifier = modifier,
            size = size,
            imageUrl = imageUrl,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun JpgPostcardAsyncImage(
    modifier: Modifier,
    size: Int,
    imageUrl: String?,
    contentDescription: String?,
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(size)
            .precision(Precision.INEXACT)
            .crossfade(true)
            .build(),
        loading = {
            Box {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        },
        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun GifPostcardAsyncImage(
    modifier: Modifier,
    size: Int,
    imageUrl: String?,
    contentDescription: String?,
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(size)
            .precision(Precision.INEXACT)
            .crossfade(true)
            .build(),
        loading = {
            Box {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        },

        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
        imageLoader = imageLoader
    )
}