package com.example.myapplication.breeds.list


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myapplication.breeds.list.BreedListContract.BreedsListEvent
import com.example.myapplication.breeds.list.BreedListContract.BreedsListState
import com.example.myapplication.breeds.list.model.BreedUiModel


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.breeds(
    route: String,
    onItemClick: (String) -> Unit,
    onBack: () -> Unit,
) = composable(
    route = route
) {
    val breedListViewModel : BreedsListViewModel  = hiltViewModel<BreedsListViewModel>()

    val state = breedListViewModel.state.collectAsState()
    BreedsListScreen(
        state = state.value,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onItemClick = onItemClick,
        onBack = onBack
    )
}

@ExperimentalMaterial3Api
@Composable
fun BreedsListScreen(
    state: BreedsListState,
    eventPublisher: (uiEvent: BreedsListEvent) -> Unit,
    onItemClick: (String) -> Unit,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            Column(
                //modifier = Modifier.padding(8.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            text = "BreedsList"
                        )
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

                Divider(
                    modifier = Modifier.padding(bottom = 8.dp)
                )


            }
        },
        content = {paddingValues ->
            Column(modifier = Modifier.fillMaxSize()) {
                //if (!state.loading){
                    TextField(
                        value = state.query,
                        onValueChange = {
                            eventPublisher(BreedsListEvent.SearchQueryChanged(it))
                        },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black),
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black
                        ),
                        placeholder = { Text("Search") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if(state.query.isNotEmpty())
                             {
                                IconButton(onClick = {

                                    eventPublisher(BreedsListEvent.ClearSearch)
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = paddingValues.calculateTopPadding())
                    )

               // }
                if (state.error != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is BreedListContract.ListError.ListUpdateFailed ->
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
                } else if (state.isSearchMode){
                    LazyForScreen(
                        breeds = state.filteredBreeds,
                        onItemClick = onItemClick,
                    )
                } else {
                    LazyForScreen(
                        breeds = state.breeds,
                        onItemClick = onItemClick,
                    )
                }
            }
        }
    )
}




@Composable
private fun LazyForScreen(
    breeds : List<BreedUiModel>,
    onItemClick: (String) -> Unit,
){
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp),
        ) {
        items(
            items = breeds,
            key = { it.id},
        ) { breed ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 16.dp)
                    .clickable { onItemClick(breed.id) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = breed.name,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = "Alternative names: ${breed.alt_names}",
                            style = TextStyle(fontSize = 16.sp),
                        )
                        Text(
                            text = "Description: ${breed.description}",
                            style = TextStyle(fontSize = 16.sp),
                        )
                        Text(
                            text = "Temperament: ",
                            style = TextStyle(fontSize = 16.sp),
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .wrapContentWidth()
                        ) {
                            breed.temperament.filter { it.isNotEmpty() }.take(3).forEach { temperament ->
                                AssistChip(
                                    modifier = Modifier.padding(end = 8.dp),
                                    onClick = {},
                                    label = { Text(text = temperament) }
                                )
                            }
                        }
                    }
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }

        }
    }
}
