package com.example.myapplication.breeds.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.myapplication.breeds.auth.AuthData
import com.example.myapplication.breeds.auth.AuthDataSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthStoreModule {

    @Provides
    @Singleton
    fun provideAuthDataStore(
        @ApplicationContext context: Context,
    ): DataStore<AuthData> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile("auth.txt") },
            serializer = AuthDataSerializer(),
        )

}