package com.example.myapplication.breeds.list

import com.example.myapplication.breeds.list.model.BreedUiModel

interface BreedListContract {

    data class BreedsListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val breeds: List<BreedUiModel> = emptyList(),
        val filteredBreeds: List<BreedUiModel> = emptyList(),
        val error: ListError? = null
    )

    sealed class BreedsListEvent {
        data class SearchQueryChanged(val query: String) : BreedsListEvent()
        data object ClearSearch : BreedsListEvent()
        data object CloseSearchMode : BreedsListEvent()
    }

    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}