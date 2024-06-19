package com.example.myapplication.breeds.leaderboard.api.di

import com.example.myapplication.breeds.leaderboard.api.LeaderboardApi
import com.example.myapplication.networking.LeaderboardApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Singleton
    @Provides
    fun provideLeaderboardApi(@LeaderboardApiClient retrofit: Retrofit) : LeaderboardApi = retrofit.create()
}