package com.example.myapplication.breeds.mappers

import com.example.myapplication.breeds.gallery.model.PhotoUiModel
import com.example.myapplication.breeds.photos.api.model.PhotoApiModel
import com.example.myapplication.breeds.photos.db.Photo

fun PhotoApiModel.asPhoto(breedId: String): Photo {
    return Photo(
        imageId = id,
        url = url ?: "",
        height = height,
        breedId = breedId
    )
}


fun Photo.asPhotoUiModel() : PhotoUiModel {
    return PhotoUiModel(
        imageId = imageId,
        url = url,
        height = height,
        breedId = breedId
    )
}