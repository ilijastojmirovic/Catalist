package com.example.myapplication.breeds.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val email: String,
)
