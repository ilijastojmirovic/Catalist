package com.example.myapplication.breeds.photos.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PhotoApiModel(
    val id: String,
    val url: String? = null,
    val height: Int
)
