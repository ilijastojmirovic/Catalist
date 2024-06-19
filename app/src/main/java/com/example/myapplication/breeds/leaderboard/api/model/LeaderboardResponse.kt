package com.example.myapplication.breeds.leaderboard.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardResponse(
    val result: PostQuizResult,
    val ranking: Int
)
