package com.example.myapplication.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.api.model.BreedApiModel
import com.example.myapplication.breeds.list.BreedListContract.BreedsListEvent
import com.example.myapplication.breeds.list.BreedListContract.BreedsListState
import com.example.myapplication.breeds.list.model.BreedUiModel
import com.example.myapplication.breeds.repository.BreedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BreedsListViewModel(
    private val repository: BreedsRepository = BreedsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsListState.() -> BreedsListState) = _state.update(reducer)

    private val events = MutableSharedFlow<BreedsListEvent>()
    fun setEvent(event: BreedsListEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchAllBreeds()
    }

    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try{
                val breeds = withContext(Dispatchers.IO){
                    repository.fetchAllBreeds().map { it.asBreedUiModel() }
                }
                setState { copy(breeds = breeds) }

            } catch (error: Exception){
                // TODO handle error
                Log.e("BreedsListViewModel", "Error fetching breeds", error)
                setState { copy(error = BreedListContract.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect{
                when(it){
                    BreedsListEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }
                    is BreedsListEvent.SearchQueryChanged -> {
                        filterBreeds(query = it.query)
                    }
                    BreedsListEvent.ClearSearch -> setState { copy(query = "", isSearchMode = false) }

                }
            }
        }
    }

    private fun filterBreeds(query: String) {
        viewModelScope.launch {
            try {
                val filteredBreeds = state.value.breeds.filter { it.name.contains(query, ignoreCase = true) }
                setState { copy(query = query, filteredBreeds = filteredBreeds, isSearchMode = true) }
            } catch (error: Exception) {
                // TODO handle error
            }
        }

    }


    private fun BreedApiModel.asBreedUiModel() = BreedUiModel(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = if (this.description.length > 250) this.description.substring(0, 250) + "..." else this.description,
        temperament = if (this.temperament.split(',').size > 3)
            this.temperament.split(',').take(3)
        else
            this.temperament.split(',')
    )
}