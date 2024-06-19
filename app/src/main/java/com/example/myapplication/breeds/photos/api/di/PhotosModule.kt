package com.example.myapplication.breeds.photos.api.di

import com.example.myapplication.breeds.photos.api.PhotosApi
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
object PhotosModule {
    @Provides
    @Singleton
    fun providePhotosApi(@CatApiClient retrofit: Retrofit): PhotosApi = retrofit.create()
}