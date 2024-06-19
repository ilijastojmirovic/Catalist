package com.example.myapplication.breeds.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.auth.AuthStore
import com.example.myapplication.breeds.leaderboard.LeaderboardContract.LeaderboardState
import com.example.myapplication.breeds.leaderboard.api.LeaderboardApi
import com.example.myapplication.breeds.leaderboard.repository.LeaderboardRepository
import com.example.myapplication.breeds.quiz.QuizContract
import com.example.myapplication.breeds.repository.BreedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()

    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) = _state.update(reducer)

    init {
        fetchResults()
    }

    private fun fetchResults(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val results = withContext(Dispatchers.IO){
                    leaderboardRepository.getAllResults()
                }

                val nickname = authStore.authData.first().nickname

                setState { copy(results = results, nickname = nickname) }

            } catch (e: Exception) {

            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}