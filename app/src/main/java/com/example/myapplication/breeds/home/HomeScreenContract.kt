package com.example.myapplication.breeds.home

interface HomeScreenContract {
    data class HomeScreenState(
        val nickname: String? = null,
    )
}