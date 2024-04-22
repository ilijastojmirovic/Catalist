package com.example.myapplication.breeds.api

import com.example.myapplication.breeds.api.model.BreedApiModel
import com.example.myapplication.breeds.api.model.BreedImageApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>

    @GET("breeds/{id}")
    suspend fun getBreed(
        @Path("id") breedId: String,
    ): BreedApiModel

    @GET("images/{id}")
    suspend fun getBreedImage(
        @Path("id") breedId: String,
    ): BreedImageApiModel
}