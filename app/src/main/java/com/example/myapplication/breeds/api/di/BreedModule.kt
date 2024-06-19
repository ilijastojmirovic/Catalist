package com.example.myapplication.breeds.api.di

import com.example.myapplication.breeds.api.BreedsApi
import com.example.myapplication.networking.CatApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BreedModule {

    @Singleton
    @Provides
    fun provideBreedApi(@CatApiClient retrofit: Retrofit) : BreedsApi{
        return retrofit.create();
    }
}