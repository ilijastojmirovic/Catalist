package com.example.myapplication.breeds.localaccount

import com.example.myapplication.breeds.auth.AuthData
import com.example.myapplication.breeds.list.BreedListContract

interface LocalAccountContract {

    data class LocalAccountState(
        val dataStoreHasContent: Boolean = false,
        val firstName: String = "",
        val lastName: String = "",
        val nickname: String = "",
        val email: String = "",
        val loading: Boolean = false
    )

    sealed class LocalAccountEvent {

        data class UpdateFirstName(val firstName: String) : LocalAccountEvent()
        data class UpdateLastName(val lastName: String) : LocalAccountEvent()
        data class UpdateNickname(val nickname: String) : LocalAccountEvent()
        data class UpdateEmail(val email: String) : LocalAccountEvent()
        data class CreateLocalAccount(val localAccount: AuthData) : LocalAccountEvent()
    }
}