package com.example.myapplication.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.breeds.details.breedDetails
import com.example.myapplication.breeds.gallery.breedGallery
import com.example.myapplication.breeds.home.homescreen
import com.example.myapplication.breeds.leaderboard.leaderboard
import com.example.myapplication.breeds.list.breeds
import com.example.myapplication.breeds.localaccount.createLocalAccount
import com.example.myapplication.breeds.profile.editprofile.editProfileScreen
import com.example.myapplication.breeds.profile.profile.profilescreen
import com.example.myapplication.breeds.quiz.quiz
import com.example.myapplication.pager.photoPager


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel = hiltViewModel<AppNavigationViewModel>()
    val state by viewModel.state.collectAsState()
    val startDestination = remember { mutableStateOf("loading") }

    LaunchedEffect(state.dataStoreHasContent) {
        startDestination.value = if (state.dataStoreHasContent) "homescreen" else "createlocalaccount"
    }


    NavHost(
            navController = navController,
            startDestination = startDestination.value
    ) {

        composable(route = "loading") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        homescreen(
            route = "homescreen",
            onListItemClick = {
                navController.navigate(route = "breeds")
            },
            onQuizItemClick = {
                navController.navigate(route = "kviz")
            },
            onLeaderboardItemClick = {
                navController.navigate(route = "leaderboard")
            },
            onProfileItemClick = {
                navController.navigate(route = "profilescreen")
            }
        )

        profilescreen(
            route = "profilescreen",
            onItemClick = {
                navController.navigate(route = "editprofilescreen")
            },
            onBack = {
                navController.navigateUp()
            }
        )

        editProfileScreen(
            route = "editprofilescreen",
            onSaveSuccess = {
                navController.navigate(route = "profilescreen")
            },
            onBack = {
                navController.navigateUp()
            }
        )

        leaderboard(
            route = "leaderboard",
            onBack = { navController.navigateUp() }
        )

        createLocalAccount(
            route = "createlocalaccount",
                    arguments = listOf(
                    navArgument("id") {
                        this.nullable = false
                        type = NavType.StringType
                    }
                    ),
            onItemClick = {
                navController.navigate(route = "homescreen")
            }
        )

        quiz(
            route = "kviz",
            onItemClick = {
                navController.navigateUp()
            }
        )

        breeds(
            route = "breeds",
            onItemClick = {
                navController.navigate(route = "breeds/$it")
            },
            onBack = {
                navController.navigateUp()
            }
        )

        breedDetails(
            route = "breeds/{id}",
            onItemClick = {
                navController.navigate(route = "breedgallery/$it")
            },
            arguments = listOf(
                navArgument("id") {
                    this.nullable = false
                    type = NavType.StringType
                }
            ),
            onBack = {
                navController.navigateUp()
            },

        )

        breedGallery(
            route = "breedgallery/{id}",
            arguments = listOf(
                navArgument("id") {
                    this.nullable = false
                    type = NavType.StringType
                }
            ),
            onBack = {
                navController.navigateUp()
            },
            onPhotoClick = { photoId->
                navController.navigate(route = "photos/$photoId")
            }
        )

        photoPager(
            route = "photos/{photo_id}",
            arguments = listOf(
                navArgument("photo_id") {
                    this.nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = {
                navController.navigateUp()
            },

        )



        }

}

inline val SavedStateHandle.breedId: String
    get() = checkNotNull(get("id")) {"breed_id is mendatory"}

inline val SavedStateHandle.photoId: String
    get() = checkNotNull(get("photo_id")) {"photo_id is mendatory"}
