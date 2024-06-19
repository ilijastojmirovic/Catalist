package com.example.myapplication.breeds.localaccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.auth.AuthStore
import com.example.myapplication.breeds.list.BreedListContract
import com.example.myapplication.breeds.localaccount.LocalAccountContract.LocalAccountEvent
import com.example.myapplication.breeds.localaccount.LocalAccountContract.LocalAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalAccountViewModel @Inject constructor(
  private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(LocalAccountState())
    val state = _state.asStateFlow()
    private fun setState(reducer: LocalAccountState.() -> LocalAccountState) = _state.update(reducer)

    private val events = MutableSharedFlow<LocalAccountEvent>()
    fun setEvent(event: LocalAccountEvent) = viewModelScope.launch { events.emit(event)}

    init {
        //checkDataStore()
        observeEvents()
    }

    private fun checkDataStore(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            val authData = authStore.authData.value
            if (authData.nickname.isNotEmpty()) {
                setState { copy(dataStoreHasContent = true, loading = false) }
            } else {
                setState { copy(loading = false) }
            }
        }
    }

    private fun observeEvents(){
        viewModelScope.launch {
            events.collect{
                when(it){
                    is LocalAccountEvent.UpdateFirstName -> setState { copy(firstName = it.firstName) }
                    is LocalAccountEvent.UpdateLastName -> setState { copy(lastName = it.lastName) }
                    is LocalAccountEvent.UpdateNickname -> setState { copy(nickname = it.nickname) }
                    is LocalAccountEvent.UpdateEmail -> setState { copy(email = it.email) }
                    is LocalAccountEvent.CreateLocalAccount -> {
                        viewModelScope.launch {
                            try {
                                authStore.updateAuthData(it.localAccount)
                                val authData = authStore.getAuthData()
                                Log.d("LocalAccountViewModel", "Read Data: $authData")

                                setState { copy(firstName = it.localAccount.firstName,
                                    lastName = it.localAccount.lastName,
                                    nickname = it.localAccount.nickname,
                                    email = it.localAccount.email) }
                            } catch (e: Exception){

                            }
                        }

                    }

                }
            }
        }
    }
}