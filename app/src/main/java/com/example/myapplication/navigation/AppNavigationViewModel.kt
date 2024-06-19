package com.example.myapplication.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.auth.AuthStore
import com.example.myapplication.navigation.AppNavigationContract.AppNavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val authStore: AuthStore
): ViewModel(){

    private val _state = MutableStateFlow(AppNavigationState())
    val state = _state.asStateFlow()
    private fun setState(reducer: AppNavigationState.() -> AppNavigationState) = _state.update(reducer)

    init {
        checkDataStore()
    }

    private fun checkDataStore() {
        viewModelScope.launch {
            val authData = authStore.authData.value
            if (authData.nickname.isNotEmpty()) {
                setState { copy(dataStoreHasContent = true) }
            }
        }
    }
}