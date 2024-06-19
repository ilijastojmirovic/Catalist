package com.example.myapplication.breeds.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import com.example.myapplication.breeds.details.BreedsDetailContract.*
import com.example.myapplication.breeds.list.BreedListContract
import com.example.myapplication.breeds.list.BreedsListScreen
import com.example.myapplication.breeds.list.BreedsListViewModel



@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.breedDetails(
    route: String,
    arguments: List<NamedNavArgument>,
    onBack: () -> Unit,
    onItemClick: (String) -> Unit,
) = composable(
    route = route
) {navBackStackEntry ->

    val breedsDetailsViewModel : BreedsDetailViewModel = hiltViewModel<BreedsDetailViewModel>(navBackStackEntry)


    val state = breedsDetailsViewModel.state.collectAsState()

    BreedDetailScreen(
        state = state.value,
        onBack = onBack,
        onItemClick = onItemClick
    )

}
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun BreedDetailScreen(
    state: BreedsDetailState,
    onBack : () -> Unit,
    onItemClick: (String) -> Unit
) {

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        state.breedsDetail?.let {
                            Text(
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                                text = it.name
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
                Divider()
            }
        },
        content = {


            val scrollState = rememberScrollState()
            if (state.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    val errorMessage = when (state.error) {
                        is BreedsDetailContract.DetailError.DetailFetchError ->
                            "Failed to load. Please try again later. Error message: ${state.error.cause?.message}."
                    }
                    Text(text = errorMessage)
                }
            }else if(state.loading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxSize()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ){
                    Spacer(modifier =Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),

                    ){
                        SubcomposeAsyncImage(


                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(8.dp)
                                ),

                            model = state.breedImageURL,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(36.dp),
                                    )
                                }

                            },
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                        Button(
                            onClick = { state.breedsDetail?.let { it1 -> onItemClick(it1.id) } },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Open Gallery")
                        }


                        Text(
                            modifier =  Modifier.padding(all = 16.dp),
                            text = "Breed: ${state.breedsDetail?.name}"
                        )
                        //Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            text = "Origin: ${state.breedsDetail?.origin}")
                        //Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            text = "Description: ${state.breedsDetail?.description}"
                        )

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            text = "Weight: Impretial(${state.breedsDetail?.weight?.imperial}), Metric(${state.breedsDetail?.weight?.metric})"
                        )

                        Text( modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                            text = "Life Span: ${state.breedsDetail?.life_span} years"
                        )

                        if(state.breedsDetail?.rare == 1){
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp),
                                text = "Common Breed"
                            )
                        } else {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp),
                                text = "Rare Breed"
                            )
                        }

                        BreedBehaviorAndNeeds(
                            adaptability = state.breedsDetail?.adaptability ?: 0,
                            affectionLevel = state.breedsDetail?.affection_level ?: 0,
                            childFriendly = state.breedsDetail?.child_friendly ?: 0,
                            dogFriendly = state.breedsDetail?.dog_friendly ?: 0,
                            energyLevel = state.breedsDetail?.energy_level ?: 0,
                            grooming = state.breedsDetail?.grooming ?: 0,
                            healthIssues = state.breedsDetail?.health_issues ?: 0,
                            intelligence = state.breedsDetail?.intelligence ?: 0,
                            sheddingLevel = state.breedsDetail?.shedding_level ?: 0,
                            socialNeeds = state.breedsDetail?.social_needs ?: 0,
                            strangerFriendly = state.breedsDetail?.stranger_friendly ?: 0,
                            vocalisation = state.breedsDetail?.vocalisation ?: 0
                        )

                        val context = LocalContext.current

                        Button(
                            onClick = {
                                state.breedsDetail?.wikipedia_url?.let { url ->
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.data = Uri.parse(url)
                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Open Wikipedia Page")
                        }


                    }
                }
            }
        }
    )


}


@Composable
fun BreedBehaviorAndNeeds(
    adaptability: Int,
    affectionLevel: Int,
    childFriendly: Int,
    dogFriendly: Int,
    energyLevel: Int,
    grooming: Int,
    healthIssues: Int,
    intelligence: Int,
    sheddingLevel: Int,
    socialNeeds: Int,
    strangerFriendly: Int,
    vocalisation: Int
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Behavior and Needs",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
        )

        Divider(Modifier.padding(vertical = 4.dp))

        BreedCharacteristic("Adaptability", adaptability)
        BreedCharacteristic("Affection Level", affectionLevel)
        BreedCharacteristic("Child Friendly", childFriendly)
        BreedCharacteristic("Dog Friendly", dogFriendly)
        BreedCharacteristic("Energy Level", energyLevel)
        BreedCharacteristic("Grooming", grooming)
        BreedCharacteristic("Health Issues", healthIssues)
        BreedCharacteristic("Intelligence", intelligence)
        BreedCharacteristic("Shedding Level", sheddingLevel)
        BreedCharacteristic("Social Needs", socialNeeds)
        BreedCharacteristic("Stranger Friendly", strangerFriendly)
        BreedCharacteristic("Vocalisation", vocalisation)
    }
}

@Composable
fun BreedCharacteristic(name: String, level: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Text(name, modifier = Modifier.weight(1f))
        LinearProgressIndicator(
            progress = level / 5f,
            modifier = Modifier
                .height(8.dp)
                .weight(2f),
            color = when (level) {
                1 -> Color.Red
                2 -> Color.Red
                3 -> Color.Yellow
                4 -> Color.Green
                5 -> Color.Green
                else -> Color.Gray
            }
        )
        Text("$level/5", modifier = Modifier.padding(start = 8.dp))
    }
}