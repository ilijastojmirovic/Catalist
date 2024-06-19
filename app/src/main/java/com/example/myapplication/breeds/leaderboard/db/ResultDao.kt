package com.example.myapplication.breeds.leaderboard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.breeds.db.Breed

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result);

    @Query("SELECT * FROM Result")
    suspend fun getAllResults(): List<Result>;
}