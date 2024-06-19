package com.example.myapplication.breeds.quiz

import com.example.myapplication.breeds.quiz.model.QuizQuestion

interface QuizContract {

    data class QuizState(
        val currentQuestionIndex: Int = 0,
        val questions: List<QuizQuestion> = emptyList(),
        val isLoading: Boolean = false,
        val quizEnded: Boolean = false,
        val score: Int = 0,
        val timeRemaining: Long = 300,
        val timeLeft: Long = 0L,
        val finalScore: Double =0.0,
        val showCancelDialog: Boolean = false,
        val showPublishDialog: Boolean = false
    )

    sealed class QuizEvent{
        data class AnswerQuestion(val answer: String) : QuizEvent()
        object ShowNextQuestion : QuizEvent()
        data object QuizCompleted : QuizEvent()
        data object TimeUp : QuizEvent()
        object CancelQuiz : QuizEvent()
        object ShowCancelDialog : QuizEvent()
        object DismissCancelDialog : QuizEvent()
        object ShowPublishDialog : QuizEvent()
        object DismissPublishDialog : QuizEvent()
        object PublishResult : QuizEvent()
    }
}