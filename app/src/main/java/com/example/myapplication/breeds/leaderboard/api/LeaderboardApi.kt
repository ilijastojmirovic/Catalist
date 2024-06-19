package com.example.myapplication.breeds.leaderboard.api

import com.example.myapplication.breeds.leaderboard.api.model.LeaderboardResponse
import com.example.myapplication.breeds.leaderboard.api.model.PostQuizResult
import com.example.myapplication.breeds.leaderboard.api.model.QuizResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") category: Int
    ): List<QuizResult>

    @POST("leaderboard")
    suspend fun postQuizResult(
        @Body quizResult: PostQuizResult
    ): LeaderboardResponse
}