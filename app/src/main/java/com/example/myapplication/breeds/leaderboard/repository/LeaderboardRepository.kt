package com.example.myapplication.breeds.leaderboard.repository

import com.example.myapplication.breeds.leaderboard.api.LeaderboardApi
import com.example.myapplication.breeds.leaderboard.api.model.LeaderboardResponse
import com.example.myapplication.breeds.leaderboard.api.model.PostQuizResult
import com.example.myapplication.breeds.leaderboard.api.model.QuizResult
import com.example.myapplication.db.AppDatabase
import javax.inject.Inject
import com.example.myapplication.breeds.leaderboard.db.Result as Result1

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase
) {
    suspend fun getAllResults(): List<QuizResult> = leaderboardApi.getLeaderboard(1)

    suspend fun addPublicResult(result: PostQuizResult): LeaderboardResponse{
        return leaderboardApi.postQuizResult(result)
    }

    suspend fun getPublicAndNonPublicResults(): List<Result1> {
        return database.resultDao().getAllResults()
    }

    suspend fun insterResult(result: Result1) {
        return database.resultDao().insertResult(result)
    }
}