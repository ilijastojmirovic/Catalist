package com.example.myapplication.breeds.leaderboard.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val category: Int,
    val nickname: String,
    val result: Double,
)
