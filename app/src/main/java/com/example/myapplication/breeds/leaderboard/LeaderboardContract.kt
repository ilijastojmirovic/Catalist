package com.example.myapplication.breeds.leaderboard

import com.example.myapplication.breeds.leaderboard.api.model.QuizResult
import com.example.myapplication.breeds.quiz.model.QuizQuestion

interface LeaderboardContract {

    data class LeaderboardState(
        val loading: Boolean = false,
        val results: List<QuizResult> = emptyList(),
        val nickname: String = ""
    )
}