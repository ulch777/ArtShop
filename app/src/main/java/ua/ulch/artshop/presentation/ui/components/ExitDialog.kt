package ua.ulch.artshop.presentation.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ua.ulch.artshop.R

@Composable
fun ExitDialog(
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)
    AlertDialog(
        onDismissRequest = onDismissClick,
        text = {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.exit_question),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onDismissClick
            ) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = { activity.finish() }) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}
