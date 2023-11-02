package ua.ulch.artshop.presentation.ui.screens.postcard_list

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ulch.artshop.data.repository.ArtShopRepository
import ua.ulch.artshop.domain.ImageActionUseCase
import ua.ulch.artshop.domain.SocialNetworks
import ua.ulch.artshop.presentation.model.PostcardModel
import ua.ulch.artshop.presentation.ui.common.SubcategoryId
import javax.inject.Inject


@HiltViewModel
class PostcardViewModel
@Inject constructor(
    private val repository: ArtShopRepository,
    private val useCase: ImageActionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostcardUiState())
    val uiState: StateFlow<PostcardUiState> = _uiState.asStateFlow()
    private var categoryId: Int? = null
    var postcards: Flow<PagingData<PostcardModel>>? = null


    var greetingText by mutableStateOf("")
        private set

    fun updateGreetingText(input: String) {
        greetingText = input
    }

    fun getPostcards(newCategoryId: Int) {
        if (categoryId == null || categoryId != newCategoryId) {
            categoryId = newCategoryId
            postcards = when (categoryId) {
                SubcategoryId.FAVOURITE_CATEGORY_ID.id -> repository.getFavouriteStream()
                    .cachedIn(viewModelScope)

                else -> repository.getPostcardStream(categoryId!!)
                    .cachedIn(viewModelScope)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeFavouriteIds()
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = e.message)
                    }
                }
                .collect { ids ->
                    _uiState.update { currentState ->
                        currentState.copy(favouriteIds = ids)
                    }
                }
        }
    }

    fun updateCurrentPosition(currentPage: Int) {
        _uiState.update { currentState -> currentState.copy(currentPosition = currentPage) }
    }

    fun updateCurrentImage(currentImage: PostcardModel?) {
        _uiState.update { currentState -> currentState.copy(currentImage = currentImage) }
    }

    fun onFavouriteClick(postcard: PostcardModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            postcard?.let {
                repository.setFavourite(postcard = postcard)
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

    fun goWeb(context: Context, url: String?) {
        url?.let {
            var webpage = Uri.parse(it)

            if (!it.startsWith("http://") && !it.startsWith("https://")) {
                webpage = Uri.parse("http://$it")
            }
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(context.packageManager) != null) {
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }
    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}