package ua.ulch.artshop.presentation.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.PostcardModel

@Composable
fun Postcard(
    modifier: Modifier,
    postcard: PostcardModel?,
    inFavorite: Boolean?,
    onFavouriteClick: (postcard: PostcardModel?) -> Unit
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.medium))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            PostcardAsyncImage(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.small))
                    .fillMaxSize(),
                size = 640,
                imageUrl = postcard?.imageUrl,
                contentDescription = "",
                isGif = postcard?.isGif == true
            )

            FavoriteItemButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                postcard = postcard,
                inFavorite = inFavorite,
                onClick = onFavouriteClick
            )
        }
    }

}

@Composable
private fun FavoriteItemButton(
    modifier: Modifier,
    postcard: PostcardModel?,
    inFavorite: Boolean?,
    onClick: (PostcardModel?) -> Unit,
) {
    IconButton(
        onClick = { onClick(postcard) },
        modifier = modifier.size(48.dp)
    ) {
        Crossfade(
            targetState = inFavorite,
            label = "",
            animationSpec = tween(300)
        ) { inFavorite ->
            when (inFavorite ?: false) {
                false -> Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = ""
                )

                true -> Icon(
                    imageVector = Icons.Filled.Favorite,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = ""
                )
            }
        }

        Icon(
            imageVector = if (inFavorite == true) Icons.Filled.Favorite
            else Icons.Filled.FavoriteBorder,
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = ""
        )
    }

}

@Preview
@Composable
fun PostcardPreview() {
    Postcard(
        modifier = Modifier,
        postcard = PostcardModel(id = 0, imageUrl = "", webUrl = ""),
        inFavorite = true,
        onFavouriteClick = {}
    )

}