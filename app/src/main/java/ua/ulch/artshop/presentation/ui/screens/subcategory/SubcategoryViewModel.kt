package ua.ulch.artshop.presentation.ui.screens.subcategory

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
class SubcategoryViewModel @Inject constructor(
    private val repository: ArtShopRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SubcategoryUiState())
    val uiState: StateFlow<SubcategoryUiState> = _uiState.asStateFlow()
    private val exceptionHandler = CoroutineExceptionHandler{ _, throwable->
        _uiState.update { currentState ->
            currentState.copy(errorMessage = throwable.message)
        }
    }

    fun getSubcategories(parentId: Int?) {
        parentId?.let {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                repository.getSubcategories(it)
            }
            viewModelScope.launch((Dispatchers.IO)) {
                subscribe(it)
            }
        }
    }

    private suspend fun subscribe(parent: Int) {
        repository.observeSubcategories(parent)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .catch { e ->
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = e.message)
                }
            }
            .collect { model ->
                _uiState.update { currentState ->
                    currentState.copy(subcategories = model, isLoading = false)
                }

            }
    }

    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}