package com.example.myapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.breeds.db.BreedDao
import com.example.myapplication.breeds.db.Breed
import com.example.myapplication.breeds.leaderboard.db.Result
import com.example.myapplication.breeds.leaderboard.db.ResultDao
import com.example.myapplication.breeds.photos.db.Photo
import com.example.myapplication.breeds.photos.db.PhotoDao

@Database(
    entities = [
        Breed::class,
        Photo::class,
        Result::class,
    ],
    version = 3,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedDao() : BreedDao;

    abstract fun photoDao() : PhotoDao;

    abstract fun resultDao() : ResultDao;
}