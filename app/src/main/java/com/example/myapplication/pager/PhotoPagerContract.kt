package com.example.myapplication.pager

import com.example.myapplication.breeds.gallery.model.PhotoUiModel
import com.example.myapplication.breeds.photos.db.Photo

interface PhotoPagerContract {

    data class PhotoPagerState(
        val photos: List<PhotoUiModel> = emptyList(),
        val clickedPhoto: Photo? = null,
        val clickedPhotoIndex: Int? = null
    )
}