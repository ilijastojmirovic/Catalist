package com.example.myapplication.breeds.quiz

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.auth.AuthStore
import com.example.myapplication.breeds.leaderboard.api.LeaderboardApi
import com.example.myapplication.breeds.leaderboard.api.model.PostQuizResult
import com.example.myapplication.breeds.leaderboard.api.model.QuizResult
import com.example.myapplication.breeds.leaderboard.db.Result
import com.example.myapplication.breeds.leaderboard.repository.LeaderboardRepository
import com.example.myapplication.breeds.list.BreedListContract
import com.example.myapplication.breeds.photos.repository.PhotoRepository
import com.example.myapplication.breeds.quiz.QuizContract.QuizState
import com.example.myapplication.breeds.repository.BreedsRepository
import com.example.myapplication.networking.LeaderboardApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class QuizViewModel@Inject constructor(
    private val repository: BreedsRepository,
    private val leaderboardRepository: LeaderboardRepository,
    private val photoRepository: PhotoRepository,
    private val authStore: AuthStore
) : ViewModel() {
    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private fun setState(reducer: QuizState.() -> QuizState) = _state.update(reducer)

    private val events = MutableSharedFlow<QuizContract.QuizEvent>()
    fun setEvent(event: QuizContract.QuizEvent) = viewModelScope.launch { events.emit(event) }

    init {
        loadQuestions()
        observeEvents()
    }

    private val timer = object: CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setState { copy(timeLeft = millisUntilFinished / 1000) }
        }
        override fun onFinish() {
            setEvent(QuizContract.QuizEvent.TimeUp)
        }
    }

    private fun observeEvents(){
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is QuizContract.QuizEvent.AnswerQuestion -> {
                        answerQuestion(it.answer)
                    }
                    is QuizContract.QuizEvent.ShowNextQuestion -> {
                        showNextQuestion()
                    }
                    is QuizContract.QuizEvent.QuizCompleted -> {
                        quizCompleted()
                    }
                    is QuizContract.QuizEvent.TimeUp -> {
                        quizCompleted()
                    }

                    is QuizContract.QuizEvent.CancelQuiz -> {
                        cancelQuiz()
                    }
                    is QuizContract.QuizEvent.ShowCancelDialog -> {
                        setState { copy(showCancelDialog = true) }
                    }
                    is QuizContract.QuizEvent.DismissCancelDialog -> {
                        setState { copy(showCancelDialog = false) }
                    }
                    is QuizContract.QuizEvent.ShowPublishDialog -> {
                        setState { copy(showPublishDialog = true) }
                    }

                    is QuizContract.QuizEvent.DismissPublishDialog ->{
                        setState { copy(showPublishDialog = false) }
                    }
                    is QuizContract.QuizEvent.PublishResult -> {
                        publishResult()
                    }

                }
            }
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                val allBreeds = repository.getAllBreeds().filter { it.id != "mala" }
                val breeds = allBreeds.take(20)
                val temperaments = allBreeds.flatMap { it.temperament.split(", ") }.distinct()

               breeds.forEach{breed ->
                   if(photoRepository.getAllPhotosForBreed(breed.id).isEmpty()){
                       withContext(Dispatchers.IO){
                           photoRepository.fetchPhotosForBreed(breed.id)
                       }
                   }
               }

                val questions = repository.generateQuizQuestions(allBreeds,breeds,temperaments)
                setState { copy(questions = questions, isLoading = false) }
                timer.start()
            } catch (e: Exception) {
                setState { copy(isLoading = false) }
                Log.e("PItanja", "USO U CATCH")
            }
        }
    }

    private fun answerQuestion(answer: String) {
        viewModelScope.launch {
            val currentQuestion = _state.value.questions[_state.value.currentQuestionIndex]
            val isCorrect = currentQuestion.correctAnswer == answer
            val newScore = if (isCorrect) _state.value.score + 1 else _state.value.score
            val nextQuestionIndex = _state.value.currentQuestionIndex + 1

            if (nextQuestionIndex < _state.value.questions.size) {
                setState { copy(currentQuestionIndex = nextQuestionIndex, score = newScore) }
                setEvent(QuizContract.QuizEvent.ShowNextQuestion)
            } else {
                setState { copy(score = newScore) }
                setEvent(QuizContract.QuizEvent.QuizCompleted)
            }
        }
    }

    private fun showNextQuestion() {
    }

    private fun quizCompleted() {
        viewModelScope.launch {

            val finalScore = _state.value.score*2.5*(1+(_state.value.timeLeft + 120)/300)
            val authData =  authStore.authData.first()
            leaderboardRepository.insterResult(
                Result(
                    category = 1,
                    nickname = authData.nickname,
                    result = finalScore.coerceAtMost(maximumValue = 100.00),
                    id = 0
                )
            )
            setState { copy(quizEnded = true, finalScore = finalScore) }
        }
    }

    private fun cancelQuiz() {
        timer.cancel()
        setState { copy(quizEnded = true, finalScore = 0.0, showCancelDialog = false) }
    }

    private fun publishResult() {
        viewModelScope.launch {
            val authData = authStore.authData.first()
            try {
                val result = leaderboardRepository.addPublicResult(
                    PostQuizResult(
                        category = 1,
                        nickname = authData.nickname,
                        result = _state.value.finalScore.coerceAtMost(maximumValue = 100.00),
                    )
                )
                Log.d("QuizViewModel", "Result published: $result")
                setState { copy(showPublishDialog = false) }
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Failed to publish result", e)
                setState { copy(showPublishDialog = false) }
            }
        }
    }

}