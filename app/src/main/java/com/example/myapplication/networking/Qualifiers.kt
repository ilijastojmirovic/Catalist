package com.example.myapplication.networking

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CatApiClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LeaderboardApiClient