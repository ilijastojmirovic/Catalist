package com.example.myapplication.pager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.gallery.PhotoGalleryContract
import com.example.myapplication.breeds.mappers.asPhotoUiModel
import com.example.myapplication.breeds.photos.repository.PhotoRepository
import com.example.myapplication.navigation.breedId
import com.example.myapplication.navigation.photoId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoPagerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository,
) : ViewModel() {

    private val photoId = savedStateHandle.photoId

    private val _state = MutableStateFlow(PhotoPagerContract.PhotoPagerState())
    val state = _state.asStateFlow()
    private fun setState(reducer : PhotoPagerContract.PhotoPagerState.() -> PhotoPagerContract.PhotoPagerState) = _state.update(reducer)

    init {
        observePhotos()
    }

    private fun observePhotos() {
        viewModelScope.launch {

            val breedId = photoRepository.getBreedIdForImage(photoId)

            val clickedImage = photoRepository.getOnePhoto(photoId);

            val photos = withContext(Dispatchers.IO){
                photoRepository.getAllPhotosForBreed(breedId = breedId)
            }

            val clickedPhotoIndex = photos.indexOfFirst { it.imageId == photoId }

            setState { copy(clickedPhotoIndex = clickedPhotoIndex) }



            photoRepository.observePhotosForBreed(breedId = breedId)
                .distinctUntilChanged()
                .collect{
                    setState { copy(photos = it.map { it.asPhotoUiModel() }, clickedPhoto = clickedImage) }
                }


        }
    }

}