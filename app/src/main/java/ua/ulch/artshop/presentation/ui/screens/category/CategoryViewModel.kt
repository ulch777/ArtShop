package ua.ulch.artshop.presentation.ui.screens.category

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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ulch.artshop.data.repository.ArtShopRepository
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: ArtShopRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()
    private val exceptionHandler = CoroutineExceptionHandler{ _, throwable->
        _uiState.update { currentState ->
            currentState.copy(errorMessage = throwable.message)
        }
    }
    init {
        viewModelScope.launch((Dispatchers.IO + exceptionHandler)) {
            _uiState.update { currentState -> currentState.copy(isLoading = true) }
            repository.getCategories(null)
            subscribe()
        }

    }

    fun refresh(){
        _uiState.update { currentState -> currentState.copy(isRefreshing = true) }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.refreshCategories()
        }
    }

    private suspend fun subscribe() {
        repository.observeCategories()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .catch { e ->
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = e.message, isRefreshing = false)
                }
            }
            .collect { model ->
                _uiState.update { currentState ->
                    currentState.copy(categories = model, isLoading = false, isRefreshing = false)
                }
            }
    }
    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}
