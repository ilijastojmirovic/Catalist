package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.breeds.details.breedDetails
import com.example.myapplication.breeds.list.breeds


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    NavHost(
            navController = navController,
            startDestination = "breeds"
    ) {

        breeds(
            route = "breeds",
            onItemClick = {
                navController.navigate(route = "breeds/$it")
            }
        )

        breedDetails(
            route = "breeds/{id}",
            arguments = listOf(
                navArgument("id") {
                    this.nullable = false
                    type = NavType.StringType
                }
            ),
            onBack = {
                navController.navigateUp()
            }
        )

//            composable(
//                route = "breeds/{id}",
//                arguments = listOf(
//                    navArgument("id") {
//                        this.nullable = false
//                        type = NavType.StringType
//                    }
//                )
//            ){
//                navBackStackEntry ->
//                val catId = navBackStackEntry.getCatDataIdOrThrow()
//                val data = remember(catId){
//                    CatRepository.getById(id = catId)
//                }
//
//                if(data != null){
//                    BreedDetailScreen(
//                        data = data,
//                        onBack = {
//                            navController.popBackStack()
//                        }
//                    )
//                }
//
//            }

        }

}

//private fun NavBackStackEntry.getCatDataIdOrThrow(): String {
//    return arguments?.getString("id") ?: throw IllegalStateException("id is required")
//}