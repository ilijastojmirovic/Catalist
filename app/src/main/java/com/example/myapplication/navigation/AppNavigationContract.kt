package com.example.myapplication.navigation

interface AppNavigationContract {
    data class AppNavigationState(
        val dataStoreHasContent: Boolean = false,
    )
}