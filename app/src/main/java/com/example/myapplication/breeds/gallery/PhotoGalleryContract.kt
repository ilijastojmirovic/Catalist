package com.example.myapplication.breeds.gallery

import com.example.myapplication.breeds.gallery.model.PhotoUiModel

interface PhotoGalleryContract {

    data class PhotoGalleryState(
        val photos: List<PhotoUiModel> = emptyList(),
    )
}