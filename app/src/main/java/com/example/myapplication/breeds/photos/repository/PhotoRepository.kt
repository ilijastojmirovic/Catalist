package com.example.myapplication.breeds.photos.repository

import com.example.myapplication.breeds.mappers.asPhoto
import com.example.myapplication.breeds.photos.api.PhotosApi
import com.example.myapplication.breeds.photos.db.Photo
import com.example.myapplication.db.AppDatabase
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photosApi: PhotosApi,
    private val database: AppDatabase,
){

    suspend fun fetchPhoto(imageId: String, breedId: String){
        val photo = photosApi.getPhoto(imageId)
        database.photoDao().insertPhoto(photo.asPhoto(breedId))
    }

    suspend fun getOnePhoto(imageId: String): Photo {
        return database.photoDao().getPhotoById(imageId)
    }

    suspend fun getAllPhotosForBreed(breedId: String): List<Photo>{
        return database.photoDao().getAllPhotosForBreed(breedId)
    }

    suspend fun fetchPhotosForBreed(breedId: String) {
        return database.photoDao().insertAllPhotosForBreed(
            photosApi.getPhotosForBreed(breedId = breedId).map { it.asPhoto(breedId) }
        )
    }

    fun observePhotosForBreed(breedId: String) = database.photoDao().observeAllBreedsPhotos(breedId)

    suspend fun getBreedIdForImage(imageId: String): String {
        return database.photoDao().getBreedIdFromImageId(imageId).breedId
    }

    suspend fun getRandomImage() : Photo {
        return database.photoDao().getRandomBreedImage()
    }
}