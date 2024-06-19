package com.example.myapplication.breeds.leaderboard.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PostQuizResult(
    val category: Int,
    val nickname: String,
    val result: Double,
)
