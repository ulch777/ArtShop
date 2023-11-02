package ua.ulch.artshop.presentation.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ulch.artshop.data.repository.ArtShopRepository
import ua.ulch.artshop.presentation.model.HolidayModel
import ua.ulch.artshop.presentation.model.MonthModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: ArtShopRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            getMonths()
                .flatMapLatest { items ->
                    _uiState.update { currentState ->
                        currentState.copy(months = items, isLoading = false)
                    }
                    getTodayHoliday()
                }
                .collect { model ->
                    _uiState.update { currentState ->
                        currentState.copy(holidays = model)
                    }
                }
        }

    }

    private suspend fun getMonths(): Flow<List<MonthModel>> = repository.observeMonths()
        .distinctUntilChanged()
        .catch { e ->
            _uiState.update { currentState ->
                currentState.copy(errorMessage = e.message)
            }
        }


    private suspend fun getTodayHoliday(): Flow<List<HolidayModel>> =
        repository.observeTodayHolidays()
            .distinctUntilChanged()
            .catch { e ->
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = e.message)
                }
            }

    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}