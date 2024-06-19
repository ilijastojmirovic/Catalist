package com.example.myapplication.db.di

import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.AppDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(builder: AppDatabaseBuilder) : AppDatabase {
        return builder.build();
    }
}