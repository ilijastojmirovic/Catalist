package com.example.myapplication.breeds.details

import com.example.myapplication.breeds.details.model.BreedsDetailUiModel

interface BreedsDetailContract {

    data class BreedsDetailState(
        val loading: Boolean = false,
        val breedsDetail: BreedsDetailUiModel? = null,
        val breedImageURL: String? = null,
        val error: DetailError? = null
    )

    sealed class BreedsDetailEvent {

    }

    sealed class DetailError {
        data class DetailFetchError(val cause: Throwable? = null) : DetailError()
    }
}