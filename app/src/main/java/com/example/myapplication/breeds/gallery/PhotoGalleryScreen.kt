package com.example.myapplication.breeds.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.material3.CircularProgressIndicator
import com.example.myapplication.breeds.compose.PhotoPreview
import com.example.myapplication.breeds.gallery.model.PhotoUiModel


fun NavGraphBuilder.breedGallery(
    route: String,
    arguments: List<NamedNavArgument>,
    onBack: () -> Unit,
    onPhotoClick: (String) -> Unit,
    ) =  composable(
        route = route
    ) {navBackStackEntry ->
        val photoGalleryViewModel = hiltViewModel<PhotoGalleryViewModel>(navBackStackEntry)

        val state = photoGalleryViewModel.state.collectAsState()

    PhotoGalleryScreen(
        state = state.value,
        onBack = onBack,
        onPhotoClick = onPhotoClick
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(
    state: PhotoGalleryContract.PhotoGalleryState,
    onBack: () -> Unit,
    onPhotoClick: (String) -> Unit,
) {

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Breed Gallery") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier,
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                if (state.photos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp),
                        columns = GridCells.Fixed(2),
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {

                        itemsIndexed(
                            items = state.photos,
                            key = { index: Int, photo: PhotoUiModel -> photo.imageId },
                        ) { index: Int, photo: PhotoUiModel ->
                            Card(
                                modifier = Modifier
                                    .size(cellSize)
                                    .clickable {
                                        onPhotoClick(photo.imageId)
                                    },
                            ) {
                                PhotoPreview(
                                    modifier = Modifier.fillMaxSize(),
                                    url = photo.url,
                                )
                            }
                        }


                    }
                }
            }
        },
    )

}
