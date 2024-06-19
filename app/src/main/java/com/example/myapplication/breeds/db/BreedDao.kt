package com.example.myapplication.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.breeds.db.Breed

@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreed(list : List<Breed>);

    @Query("SELECT * FROM Breed")
    suspend fun getAllBreeds(): List<Breed>;

    @Query("SELECT * FROM Breed WHERE id = :id")
    suspend fun getBreedById(id: String): Breed;
}