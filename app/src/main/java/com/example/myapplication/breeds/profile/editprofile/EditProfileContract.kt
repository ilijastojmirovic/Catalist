package com.example.myapplication.breeds.profile.editprofile

interface EditProfileContract {

    data class EditProfileState(
        val firstName: String = "",
        val lastName: String = "",
        val nickname: String = "",
        val email: String = "",
        val loading: Boolean = false,
        //val isSaved: Boolean = false,
    )

    sealed class EditProfileEvent {
        data class UpdateFirstName(val firstName: String) : EditProfileEvent()
        data class UpdateLastName(val lastName: String) : EditProfileEvent()
        data class UpdateNickname(val nickname: String) : EditProfileEvent()
        data class UpdateEmail(val email: String) : EditProfileEvent()
        object SaveProfile : EditProfileEvent()
    }
}