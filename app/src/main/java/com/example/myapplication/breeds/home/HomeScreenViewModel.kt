package com.example.myapplication.breeds.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.auth.AuthStore
import com.example.myapplication.breeds.home.HomeScreenContract.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel(){

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private fun setState(reducer: HomeScreenState.() -> HomeScreenState) = _state.update(reducer)

    init {
        loadAuthSoreInfo()
    }

    private fun loadAuthSoreInfo(){
        viewModelScope.launch {
            val authData = authStore.authData.first()

            setState { copy(nickname = authData.nickname) }
        }
    }
}