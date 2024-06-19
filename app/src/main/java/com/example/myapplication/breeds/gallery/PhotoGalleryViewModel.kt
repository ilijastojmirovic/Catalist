package com.example.myapplication.breeds.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.gallery.PhotoGalleryContract.PhotoGalleryState
import com.example.myapplication.breeds.gallery.model.PhotoUiModel
import com.example.myapplication.breeds.mappers.asPhotoUiModel
import com.example.myapplication.breeds.photos.repository.PhotoRepository
import com.example.myapplication.navigation.breedId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository,
) : ViewModel() {

    private val breedId = savedStateHandle.breedId

    private val _state = MutableStateFlow(PhotoGalleryState())
    val state = _state.asStateFlow()
    private fun setState(reducer : PhotoGalleryState.() -> PhotoGalleryState) = _state.update(reducer)

    init {
        observePhotos()
    }

    private fun observePhotos() {
        viewModelScope.launch {
            photoRepository.observePhotosForBreed(breedId = breedId)
                .distinctUntilChanged()
                .collect{
                    setState { copy(photos = it.map { it.asPhotoUiModel() }) }
                }
        }
    }
}