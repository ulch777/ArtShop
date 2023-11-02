package ua.ulch.artshop.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ulch.artshop.data.repository.ArtShopRepository
import ua.ulch.artshop.presentation.model.LanguageModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ArtShopRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            subscribe()
        }
    }

    fun setSelectedLang(langId: String) {
        repository.setCurrentLocale(langId)
    }

    fun getLanguageList(): List<LanguageModel> = repository.getLanguageList()

    private suspend fun subscribe() {
        repository.observeCurrentLocale()
            .distinctUntilChanged()
            .catch { e -> e.printStackTrace() }
            .collect { lang ->
                _uiState.update { currentState ->
                    currentState.copy(selectedLangId = lang)
                }
            }
    }

    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}