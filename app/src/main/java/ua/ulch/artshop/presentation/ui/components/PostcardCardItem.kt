package ua.ulch.artshop.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.PostcardModel

@Composable
fun PostcardCardItem(
    modifier: Modifier,
    postcard: PostcardModel?,
    onPostcardClicked: () -> Unit,
) {
    Card(
        modifier = modifier
            .clickable { onPostcardClicked() }
    ) {
        PostcardAsyncImage(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.small))
                .height(200.dp)
                .fillMaxSize(),
            size = 320,
            imageUrl = postcard?.imageUrl,
            contentDescription = "",
            isGif = postcard?.isGif == true
        )
    }

}

@Preview
@Composable
fun PostcardCardItemPreview() {
    PostcardCardItem(
        modifier = Modifier,
        postcard = PostcardModel(imageUrl = ""),
        onPostcardClicked = {}
    )
}