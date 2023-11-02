package ua.ulch.artshop.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import ua.ulch.artshop.R

@Composable
fun RateDialog(
    modifier: Modifier = Modifier,
    onRateClick: () -> Unit,
    onLaterClick: () -> Unit,
    onDismissClick: () -> Unit,

) {
    Dialog(
        onDismissRequest = {},
    ) {
        Card {
            Column(modifier = modifier.padding(dimensionResource(id = R.dimen.small))) {
                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.rate_us)
                            + " \"" + stringResource(R.string.app_name) + "\"",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = modifier
                        .padding(vertical = dimensionResource(id = R.dimen.small))
                        .fillMaxWidth(),
                    text = stringResource(R.string.if_you_like)
                            + " \"" + stringResource(R.string.app_name) + "\""
                            + " " + stringResource(
                        R.string.please_rate_us
                    ),
                    textAlign = TextAlign.Center,
                )
                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = { onRateClick() }) {
                    Text(text = stringResource(id = R.string.rate_us))

                }
                OutlinedButton(
                    modifier = modifier.fillMaxWidth(),
                    onClick = { onLaterClick() }) {
                    Text(text = stringResource(id = R.string.remind_later))

                }
                OutlinedButton(
                    modifier = modifier.fillMaxWidth(),
                    onClick = { onDismissClick() }) {
                    Text(text = stringResource(id = R.string.no_thanks))

                }
            }
        }
    }
}

@Preview
@Composable
fun RateDialogPreview() {
    RateDialog(
        onRateClick = { },
        onLaterClick = {},
        onDismissClick = {}
    )
}