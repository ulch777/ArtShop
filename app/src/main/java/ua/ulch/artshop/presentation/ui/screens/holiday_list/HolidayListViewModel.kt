package ua.ulch.artshop.presentation.ui.screens.holiday_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ulch.artshop.data.repository.ArtShopRepository
import ua.ulch.artshop.presentation.ui.navigation.MONTH_ID_KEY
import javax.inject.Inject

@HiltViewModel
class HolidayListViewModel @Inject constructor(
    private val repository: ArtShopRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HolidayListUiState())
    val uiState: StateFlow<HolidayListUiState> = _uiState.asStateFlow()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { currentState ->
            currentState.copy(errorMessage = throwable.message)
        }
    }

    init {
        val monthId = savedStateHandle.get<Int>(MONTH_ID_KEY)
        _uiState.update { currentState -> currentState.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            monthId?.let {
                repository.getHolidays(monthId)
                subscribe(monthId)
            }
        }
    }

    private suspend fun subscribe(monthId: Int) {
        repository.observeHolidays(monthId)
            .distinctUntilChanged()
            .catch { e ->
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = e.message)
                }
            }
            .collect { model ->
                model.sortedBy { it.id }
                _uiState.update { currentState ->
                    currentState.copy(holidays = model, isLoading = false)
                }
            }
    }
    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}