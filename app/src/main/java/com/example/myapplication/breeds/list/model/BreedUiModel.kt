package com.example.myapplication.breeds.list.model

data class BreedUiModel(
    val id: String,
    val name: String,
    val alt_names: String = "",
    val description: String,
    val temperament: List<String>,
)
