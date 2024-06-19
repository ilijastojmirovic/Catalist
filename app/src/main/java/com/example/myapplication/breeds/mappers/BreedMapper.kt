package com.example.myapplication.breeds.mappers

import com.example.myapplication.breeds.api.BreedsApi
import com.example.myapplication.breeds.api.model.BreedApiModel
import com.example.myapplication.breeds.api.model.Weight
import com.example.myapplication.breeds.db.Breed
import com.example.myapplication.breeds.details.model.BreedsDetailUiModel
import com.example.myapplication.breeds.list.model.BreedUiModel



fun Breed.asBreedUiModel() : BreedUiModel {
    return BreedUiModel(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament = this.temperament.split(", "),
    )
}


fun BreedApiModel.asBreed() : Breed {
    return Breed(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament = this.temperament,
        origin = this.origin,
        life_span = this.life_span,
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


fun Breed.asBreedsDetailUiModel() : BreedsDetailUiModel {
    return BreedsDetailUiModel(
        id = this.id,
        name = this.name,
        temperament = this.temperament,
        origin = this.origin,
        description = this.description,
        life_span = this.life_span,
        weight = Weight(imperial = "", metric = ""),
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

