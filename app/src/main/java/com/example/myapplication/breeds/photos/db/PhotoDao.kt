package com.example.myapplication.breeds.photos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo : Photo)

    @Query("SELECT * FROM Photo WHERE imageId = :imageId")
    suspend fun getPhotoById(imageId: String): Photo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPhotosForBreed(list : List<Photo>)

    @Query("SELECT * FROM Photo WHERE breedId = :breedId")
    suspend fun getAllPhotosForBreed(breedId: String): List<Photo>

    @Query("SELECT * FROM Photo WHERE breedId = :breedId")
    fun observeAllBreedsPhotos(breedId: String): Flow<List<Photo>>

    @Query("SELECT * FROM Photo WHERE imageId = :imageId")
    suspend fun getBreedIdFromImageId(imageId: String): Photo

    @Query("SELECT * FROM Photo ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomBreedImage(): Photo
}