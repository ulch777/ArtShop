package ua.ulch.artshop.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ulch.artshop.data.repository.ArtShopRepository
import ua.ulch.artshop.domain.ImageActionUseCase
import ua.ulch.artshop.domain.SocialNetworks
import ua.ulch.artshop.presentation.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ArtShopRepository,
    private val useCase: ImageActionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    private var langId: String? = null

    init {
        viewModelScope.launch {
            appLaunched()
        }
        viewModelScope.launch {
            observeDrawerList()
        }
    }

    private suspend fun observeDrawerList() {
        repository.observeCurrentLocale()
            .distinctUntilChanged()
            .catch { e -> e.printStackTrace() }
            .collect { lang ->
                _uiState.update { currentState ->
                    currentState.copy(localeChanged = langId != null && lang != langId)
                }
                langId = lang
            }
    }

    fun getCurrentLocale(): String = repository.getCurrentLocale()

    fun changeState(state: UiState<String>) {
        _uiState.update { currentState -> currentState.copy(state = state) }
    }

    fun setTitle(title: String) {
        _uiState.update { currentState -> currentState.copy(title = title) }
    }

    fun showExitDialog(boolean: Boolean) {
        _uiState.update { currentState -> currentState.copy(showExitDialog = boolean) }
    }

    fun showRateDialog(boolean: Boolean) {
        _uiState.update { currentState -> currentState.copy(showRateDialog = boolean) }
    }

    fun setNeverRate() {
        _uiState.update { currentState -> currentState.copy(showRateDialog = false) }
        repository.setDoNotAskRate()
    }

    private fun appLaunched() {
        if (repository.checkAskRate()) {
            _uiState.update { currentState -> currentState.copy(showRateDialog = true) }
        }
    }

    fun resetLocaleChange() {
        viewModelScope.launch { repository.resetData() }
        _uiState.update { currentState ->
            currentState.copy(localeChanged = false)
        }
    }

    fun resetErrorState() {
        _uiState.update { currentState -> currentState.copy(state = null) }
    }

    fun getInviteIntent(socialNetworks: SocialNetworks) {
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(invitationIntent = useCase.createInvitation(socialNetworks))
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(state = UiState.Error(e.message))
                }
            }
        }
    }

    fun resetInviteIntent() {
        _uiState.update { currentState ->
            currentState.copy(invitationIntent = null)
        }
    }
}