package com.example.myapplication.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myapplication.breeds.compose.PhotoPreview


fun NavGraphBuilder.photoPager(
    route: String,
    arguments: List<NamedNavArgument>,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) {navBackStackEntry ->

    val photoPagerViewModel = hiltViewModel<PhotoPagerViewModel>(navBackStackEntry)

    val state = photoPagerViewModel.state.collectAsState()

    val initialPage = navBackStackEntry.arguments?.getInt("initialPage") ?: 0

    PhotoPagerScreen(
        state = state.value,
        onClose = onClose,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoPagerScreen(
    state: PhotoPagerContract.PhotoPagerState,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            state.photos.size
        }
    )




    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            if (state.photos.isNotEmpty()) {
//                LaunchedEffect(pagerState) {
//                    snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
//                        val breed = state.photos.getOrNull(pageIndex)
//                        currentTitle = breed?.breedId ?: ""
//                    }
//                }

                LaunchedEffect(state.photos, state.clickedPhotoIndex) {
                    if (state.photos.isNotEmpty()) {
                        state.clickedPhotoIndex?.let { pagerState.scrollToPage(it) }
                    }
                }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = {
                        state.photos[it].imageId
                    },
                ) { pageIndex ->
                    val photo = state.photos[pageIndex]
                    println(pageIndex)
                    PhotoPreview(
                        modifier = Modifier,
                        url = photo.url,
                    )
                }

            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No photos.",
                )
            }
        },
    )
}