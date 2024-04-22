package com.example.myapplication.breeds.api.model

import kotlinx.serialization.Serializable

@Serializable
data class BreedImageApiModel(
    val id: String,
    val url: String,
)