package com.example.myapplication.breeds.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myapplication.breeds.leaderboard.LeaderboardViewModel
import com.example.myapplication.breeds.quiz.QuizContract
import kotlinx.coroutines.launch

fun NavGraphBuilder.homescreen(
    route: String,
    onQuizItemClick: () -> Unit,
    onListItemClick: () -> Unit,
    onLeaderboardItemClick: () -> Unit,
    onProfileItemClick: () -> Unit
) = composable(
    route = route,
    ) {

    val homeScreenViewModel : HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>()

    val state = homeScreenViewModel.state.collectAsState()

    HomeScreen(
        state = state.value,
        onQuizItemClick = onQuizItemClick,
        onListItemClick = onListItemClick,
        onLeaderboardItemClick = onLeaderboardItemClick,
        onProfileItemClick = onProfileItemClick
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenContract.HomeScreenState,
    onQuizItemClick: () -> Unit,
    onListItemClick: () -> Unit,
    onLeaderboardItemClick: () -> Unit,
    onProfileItemClick: () -> Unit
) {

    val uiScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {
            HomeScreenDrawer(
                state = state,
                onProfileClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onProfileItemClick()
                },
                onQuizClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onQuizItemClick()
                },
                onLeaderboardItemClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onLeaderboardItemClick()
                }
            )
        },
        content = {
            LargeTopAppBar(
                title = {
                        Text(
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            text = "Catapult",
                            modifier = Modifier
                                .padding(bottom = 16.dp, start = 127.dp)
                        )
                    }
                ,
                navigationIcon = {
                    IconButton(onClick = { uiScope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = onQuizItemClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(text = "Pocni Kviz")
                        }
                        Button(
                            onClick = onListItemClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Nauci o mackama")
                        }
                        Button(
                            onClick = onLeaderboardItemClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Lista javnih rezultata")
                        }
                        Button(
                            onClick = onProfileItemClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Moj profil")
                        }
                    }
                }
        }
    )


}

@Composable
private fun AppDrawerActionItem(
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() }
        ),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        headlineContent = {
            Text(text = text)
        }
    )
}

@Composable
private fun HomeScreenDrawer(
    state: HomeScreenContract.HomeScreenState,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardItemClick: () -> Unit,
) {
    BoxWithConstraints {
        // We can use ModalDrawerSheet as a convenience or
        // built our own drawer as AppDrawer example
        ModalDrawerSheet(
            modifier = Modifier.width(maxWidth * 3 / 4),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    state.nickname?.let {
                        Text(
                            modifier = Modifier.padding(all = 16.dp),
                            text = it,
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {

                    AppDrawerActionItem(
                        icon = Icons.Default.Person,
                        text = "Profile",
                        onClick = onProfileClick,
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.PlayArrow,
                        text = "Kviz",
                        onClick = onQuizClick,
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Info,
                        text = "Lista javnih rezultata",
                        onClick = onLeaderboardItemClick,
                    )
                }


            }
        }
    }
}