package ua.ulch.artshop.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.domain.SocialNetworks
import ua.ulch.artshop.presentation.ui.common.debounceClickable

@Composable
fun InviteCard(
    modifier: Modifier,
    onInviteClick: (
        shareType: SocialNetworks,
    ) -> Unit
) {
    Row(modifier = modifier
        .padding(dimensionResource(id = R.dimen.very_large))
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround) {
        Image(
            modifier = modifier
                .size(48.dp)
                .debounceClickable {
                    onInviteClick(SocialNetworks.TELEGRAM)
                },
            painter = painterResource(id = R.drawable.ic_telegram),
            contentDescription = ""
        )
        Image(
            modifier = modifier
                .size(48.dp)
                .debounceClickable {
                    onInviteClick(SocialNetworks.VIBER)
                },
            painter = painterResource(id = R.drawable.ic_viber),
            contentDescription = ""
        )
        Image(
            modifier = modifier
                .size(48.dp)
                .debounceClickable {
                    onInviteClick(SocialNetworks.WHATSAPP)
                },
            painter = painterResource(id = R.drawable.ic_whatsapp),
            contentDescription = ""
        )
        Image(
            modifier = modifier
                .size(48.dp)
                .debounceClickable {
                    onInviteClick(SocialNetworks.NOT_SPECIFIED)
                },
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = ""
        )
    }
}

@Preview
@Composable
fun InviteCardPreview(){
    InviteCard(modifier = Modifier, onInviteClick = {})
}
