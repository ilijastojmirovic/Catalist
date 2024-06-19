package com.example.myapplication.breeds.profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.auth.AuthStore
import com.example.myapplication.breeds.leaderboard.repository.LeaderboardRepository
import com.example.myapplication.breeds.profile.profile.ProfileContract.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    private fun setState(reducer: ProfileState.() -> ProfileState) = _state.update(reducer)

    init {
        observeResults()
    }

    private fun observeResults(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val results = withContext(Dispatchers.IO){
                    leaderboardRepository.getPublicAndNonPublicResults()
                }

                val authData = authStore.authData.value

                setState { copy(results = results, profileInfo = authData) }
            } catch (e: Exception){

            } finally {
                setState { copy(loading = false) }
            }

        }
    }


}