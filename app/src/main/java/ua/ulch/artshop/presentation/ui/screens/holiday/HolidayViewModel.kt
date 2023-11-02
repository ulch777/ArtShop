package ua.ulch.artshop.presentation.ui.screens.holiday

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
import ua.ulch.artshop.domain.ImageActionUseCase
import ua.ulch.artshop.domain.SocialNetworks
import ua.ulch.artshop.presentation.ui.navigation.HOLIDAY_ID_KEY
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val repository: ArtShopRepository,
    private val useCase: ImageActionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HolidayUiState())
    val uiState: StateFlow<HolidayUiState> = _uiState.asStateFlow()

    var greetingText by mutableStateOf("")
        private set

    fun updateGreetingText(input: String) {
        greetingText = input
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val holidayId = savedStateHandle.get<Int>(HOLIDAY_ID_KEY)
            holidayId?.let {
                repository.getHoliday(holidayId)
                    .distinctUntilChanged()
                    .catch { e ->
                        _uiState.update { currentState ->
                            currentState.copy(errorMessage = e.message)
                        }
                    }
                    .collect { model ->
                        _uiState.update { currentState ->
                            currentState.copy(holiday = model)
                        }
                    }
            }
        }
    }

    fun downloadImageFromUrl(url: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val downloaded = useCase.downloadImage(url = url)
                _uiState.update { currentState ->
                    currentState.copy(imageDownloaded = downloaded)
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = e.message)
                }
            }
        }

    }

    fun shareImage(
        shareType: SocialNetworks,
        imageUrl: String?,
        greetingText: String
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val shareImageObject =
                    useCase.getShareImageObject(
                        text = greetingText,
                        imageUrl = imageUrl,
                        socialNetworks = shareType
                    )
                _uiState.update { currentState ->
                    currentState.copy(shareImageObject = shareImageObject)
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = e.message)
                }
            }
        }
    }

    fun resetIsDownloaded() {
        _uiState.update { currentState ->
            currentState.copy(imageDownloaded = null)
        }
    }

    fun resetShareImageObject() {
        _uiState.update { currentState ->
            currentState.copy(shareImageObject = null)
        }
    }

    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}