package com.example.myapplication.breeds.quiz.model

data class QuizQuestion(
    val question: String,
    val imageUrl: String,
    val options: List<String>,
    val correctAnswer: String
)
