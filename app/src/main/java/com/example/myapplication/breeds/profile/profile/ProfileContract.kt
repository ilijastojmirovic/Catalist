package com.example.myapplication.breeds.profile.profile

import com.example.myapplication.breeds.auth.AuthData
import com.example.myapplication.breeds.leaderboard.db.Result

interface ProfileContract {

    data class ProfileState(
        val loading: Boolean = false,
        val results: List<Result> = emptyList(),
        val profileInfo: AuthData? = null,
    )
}