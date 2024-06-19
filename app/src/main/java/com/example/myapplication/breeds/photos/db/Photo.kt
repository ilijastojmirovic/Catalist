package com.example.myapplication.breeds.photos.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Photo(
    @PrimaryKey val imageId: String,
    val url: String,
    val height: Int,
    val breedId: String
)
