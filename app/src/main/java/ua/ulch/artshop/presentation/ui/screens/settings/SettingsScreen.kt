package ua.ulch.artshop.presentation.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.LanguageModel
import ua.ulch.artshop.presentation.ui.common.UiState

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onStateChanged: (UiState<String>) -> Unit
) {
    val itemsList = viewModel.getLanguageList()
    val uiState by viewModel.uiState.collectAsState()
    uiState.errorMessage?.let {
        onStateChanged(UiState.Error(it))
        viewModel.resetErrorMessage()
    }
    LanguageColumn(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.small)),
        itemsList = itemsList,
        selected = uiState.selectedLangId,
        setSelected = { langId -> viewModel.setSelectedLang(langId) }
    )
}

@Composable
fun LanguageColumn(
    modifier: Modifier,
    itemsList: List<LanguageModel>,
    selected: String?,
    setSelected: (String) -> Unit
) {

    LazyColumn(modifier = modifier) {
        items(itemsList) { item ->
            LanguageItem(
                item = item,
                isSelected = selected == item.langId,
                setSelected = setSelected
            )

        }
    }
}

@Composable
fun LanguageItem(
    item: LanguageModel,
    isSelected: Boolean,
    setSelected: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.medium))
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.small)),
                    text = stringResource(id = item.nameId),
                    style = MaterialTheme.typography.titleMedium,
                )
                Image(painter = painterResource(id = item.resId), contentDescription = "")
            }
            RadioButton(selected = isSelected, onClick = { setSelected(item.langId) })
        }
        Divider(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.very_small)),
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
fun LanguageItemPreview() {
    LanguageItem(
        item = LanguageModel(
            R.string.ukrainian,
            R.drawable.ukraine,
            "uk"
        ),
        isSelected = true,
        setSelected = {}
    )
}