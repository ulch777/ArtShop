package ua.ulch.artshop.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.NavigationDrawerModel
import ua.ulch.artshop.presentation.ui.common.debounceClickable

@Composable
fun DrawerContent(
    itemsList: List<NavigationDrawerModel>,
    itemClick: (NavigationDrawerModel) -> Unit
) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 36.dp)
    ) {

        items(itemsList) { item ->
            NavigationListItem(item = item) {
                itemClick(item)
            }
        }
    }
}

@Composable
private fun NavigationListItem(
    item: NavigationDrawerModel,
    itemClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .debounceClickable {
                    itemClick()
                }
                .padding(
                    horizontal = dimensionResource(id = R.dimen.large),
                    vertical = dimensionResource(id = R.dimen.medium)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium)),
                text = item.label ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            item.resId?.let {
                Image(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.small))
                        .size(24.dp),
                    painter = painterResource(id = item.resId),
                    contentDescription = ""
                )
            }

        }
        Divider(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.very_small)),
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp
        )
    }

}

