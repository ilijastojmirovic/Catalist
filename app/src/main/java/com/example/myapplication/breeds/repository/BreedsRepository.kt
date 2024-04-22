package com.example.myapplication.breeds.repository

import com.example.myapplication.breeds.api.BreedsApi
import com.example.myapplication.breeds.api.model.BreedApiModel
import com.example.myapplication.networking.retrofit

object BreedsRepository {

    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

    suspend fun fetchAllBreeds(): List<BreedApiModel> {
        val breeds = breedsApi.getAllBreeds()
        return breeds
    }

    suspend fun fetchBreedById(breedId: String) : BreedApiModel {
        val breed = breedsApi.getBreed(breedId)
        return breed
    }

    suspend fun fetchBreedImageById(imageId: String) : String {
        val breedImage = breedsApi.getBreedImage(imageId)
        return breedImage.url
    }
}