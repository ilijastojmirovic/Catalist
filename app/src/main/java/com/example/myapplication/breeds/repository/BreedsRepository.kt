package com.example.myapplication.breeds.repository

import android.util.Log
import com.example.myapplication.breeds.api.BreedsApi
import com.example.myapplication.breeds.api.model.BreedApiModel
import com.example.myapplication.breeds.db.Breed
import com.example.myapplication.breeds.mappers.asBreed
import com.example.myapplication.breeds.photos.db.Photo
import com.example.myapplication.breeds.photos.repository.PhotoRepository
import com.example.myapplication.breeds.quiz.model.QuizQuestion
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class BreedsRepository @Inject constructor(
    private val breedsApi: BreedsApi,
    private val database: AppDatabase,
    private val photoRepository: PhotoRepository
){

    suspend fun getAllBreeds() : List<Breed> {
        return database.breedDao().getAllBreeds();
    }

    suspend fun fetchAllBreeds(): List<BreedApiModel> {
        val breeds = breedsApi.getAllBreeds()
        database.breedDao().insertBreed(breeds.map { it.asBreed() })

//        breeds.forEach { breed ->
//                photoRepository.fetchPhotosForBreed(breed.id)
//        }
        return breeds
    }

    suspend fun getBreedById(breedId: String): Breed {
        return database.breedDao().getBreedById(breedId)
    }

    suspend fun generateQuizQuestions(allBreeds: List<Breed>,breeds: List<Breed>, temperaments: List<String>): List<QuizQuestion> {

        val questions = mutableListOf<QuizQuestion>()
//        val usedImages = mutableSetOf<String>()

        breeds.forEach { breed->
            val breedImage = photoRepository.getAllPhotosForBreed(breedId = breed.id).shuffled().first()
            println(breedImage)

            val imageUrl = breedImage.url

            //Log.d("Metoda", breedImages.toString())
//            if (!usedImages.contains(imageUrl)) {
//                usedImages.add(imageUrl)

                val question = when (Random.nextInt(3)) {
                    0 -> generateGuessTheBreedQuestion(breed, allBreeds, imageUrl)
                    1 -> generateSpotOddTemperamentQuestion(breed, imageUrl, temperaments)
                    else -> generateCorrectTemperamentQuestion(breed, imageUrl, temperaments)
                }

                questions.add(question)
            //}
        }

        Log.e("PITANJA", questions.toString())
        return questions.shuffled()
    }

    private fun generateGuessTheBreedQuestion(correctBreed: Breed, allBreeds: List<Breed>, correctBreedImageUrl: String): QuizQuestion {
        val options = mutableListOf(correctBreed.name)
        options.addAll(allBreeds.filter { it.id != correctBreed.id }.shuffled().take(3).map { it.name })
        options.shuffle()

        return QuizQuestion(
            question = "Koja je rasa mačke?",
            imageUrl = correctBreedImageUrl,
            options = options,
            correctAnswer = correctBreed.name
        )
    }

    private fun generateSpotOddTemperamentQuestion(breed: Breed, imageUrl: String, allTemperaments: List<String>): QuizQuestion {
        val breedTemperaments = breed.temperament.split(", ")
        val correctTemperament = allTemperaments
            .filter { it !in breedTemperaments }
            .random()
        val otherTemperaments = breedTemperaments.shuffled().take(3)
        val options = (otherTemperaments + correctTemperament).shuffled()


        return QuizQuestion(
            question = "Jedan od ovih temperamenata ne opisuje mačku sa slike. Izbaci uljeza!",
            imageUrl = imageUrl,
            options = options,
            correctAnswer = correctTemperament
        )
    }

    private fun generateCorrectTemperamentQuestion(breed: Breed, imageUrl: String, allTemperaments: List<String>): QuizQuestion {
        val correctTemperament = breed.temperament.split(", ").random()
        val otherTemperaments = allTemperaments.filter { it != correctTemperament }.shuffled().take(3)
        val options = (otherTemperaments + correctTemperament).shuffled()

        return QuizQuestion(
            question = "Koji temperament pripada zadatoj mački?",
            imageUrl = imageUrl,
            options = options,
            correctAnswer = correctTemperament
        )
    }
}


//    suspend fun fetchBreedById(breedId: String) : BreedApiModel {
//        val breed = breedsApi.getBreed(breedId)
//        return breed
//    }
//
//    suspend fun fetchBreedImageById(imageId: String) : String {
//        val breedImage = breedsApi.getBreedImage(imageId)
//        return breedImage.url
//    }