package com.example.myapplication.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.breeds.api.model.BreedApiModel
import com.example.myapplication.breeds.api.model.Weight
import com.example.myapplication.breeds.details.BreedsDetailContract.BreedsDetailEvent
import com.example.myapplication.breeds.details.BreedsDetailContract.BreedsDetailState
import com.example.myapplication.breeds.details.model.BreedsDetailUiModel
import com.example.myapplication.breeds.list.BreedListContract
import com.example.myapplication.breeds.repository.BreedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BreedsDetailViewModel(
    private val breedId : String,
    private val repository: BreedsRepository = BreedsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BreedsDetailState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsDetailState.() -> BreedsDetailState) = _state.update(reducer)

    private val events = MutableSharedFlow<BreedsDetailEvent>()
    fun setEvent(event: BreedsDetailEvent) = viewModelScope.launch { events.emit(event) }

    init {
        fetchBreedDetails()
    }

    private fun fetchBreedDetails() {
        viewModelScope.launch {

            setState { copy(loading = true) }
            try {
                val breed = withContext(Dispatchers.IO) {
                     repository.fetchBreedById(breedId).asBreedsDetailUiModel()
                }
                val breedImage = withContext(Dispatchers.IO) {
                    breed.reference_image_id?.let { repository.fetchBreedImageById(it) }
                }
                setState { copy(breedsDetail = breed) }
                setState { copy(breedImageURL = breedImage) }


            } catch (error: Exception) {
                // TODO Handle error
                setState { copy(error = BreedsDetailContract.DetailError.DetailFetchError(cause = error)) }
            } finally {
                setState { copy(loading = false)}
            }
        }

    }

    private fun BreedApiModel.asBreedsDetailUiModel() = BreedsDetailUiModel(
        id = this.id,
        name = this.name,
        temperament = this.temperament,
        origin = this.origin,
        description = this.description,
        life_span = this.life_span,
        weight = this.weight,
        rare = this.rare,
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        dog_friendly = this.dog_friendly,
        energy_level = this.energy_level,
        grooming = this.grooming,
        health_issues = this.health_issues,
        intelligence = this.intelligence,
        shedding_level = this.shedding_level,
        social_needs = this.social_needs,
        stranger_friendly = this.stranger_friendly,
        vocalisation = this.vocalisation,
        reference_image_id = this.reference_image_id,
        wikipedia_url = this.wikipedia_url,
    )
}