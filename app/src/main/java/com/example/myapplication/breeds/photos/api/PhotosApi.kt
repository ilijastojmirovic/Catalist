package com.example.myapplication.breeds.photos.api

import com.example.myapplication.breeds.photos.api.model.PhotoApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosApi {

    @GET("images/{id}")
    suspend fun getPhoto(
        @Path("id") photoId: String?,
    ): PhotoApiModel

    @GET("images/search")
    suspend fun getPhotosForBreed(
        @Query("limit") limit: Int = 100,
        @Query("breed_ids") breedId: String
    ): List<PhotoApiModel>
}