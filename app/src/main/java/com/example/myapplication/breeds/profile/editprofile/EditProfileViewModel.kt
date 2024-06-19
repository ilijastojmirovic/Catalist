package com.example.myapplication.breeds.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.auth.AuthData
import com.example.myapplication.breeds.auth.AuthStore
import com.example.myapplication.breeds.profile.editprofile.EditProfileContract.EditProfileEvent
import com.example.myapplication.breeds.profile.editprofile.EditProfileContract.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    private fun setState(reducer: EditProfileState.() -> EditProfileState) = _state.update(reducer)

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val authData = authStore.authData.first()
            setState {
                copy(
                    firstName = authData.firstName,
                    lastName = authData.lastName,
                    nickname = authData.nickname,
                    email = authData.email
                )
            }
        }
    }

    fun setEvent(event: EditProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is EditProfileEvent.UpdateFirstName -> setState { copy(firstName = event.firstName) }
                is EditProfileEvent.UpdateLastName -> setState { copy(lastName = event.lastName) }
                is EditProfileEvent.UpdateNickname -> setState { copy(nickname = event.nickname) }
                is EditProfileEvent.UpdateEmail -> setState { copy(email = event.email) }
                is EditProfileEvent.SaveProfile -> saveProfile()
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
                authStore.updateAuthData(
                    AuthData(
                        firstName = _state.value.firstName,
                        lastName = _state.value.lastName,
                        nickname = _state.value.nickname,
                        email = _state.value.email
                    )
                )
               // setState { copy(isSaved = true) }
        }
    }

}