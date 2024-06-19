package com.example.myapplication.breeds.profile.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myapplication.breeds.auth.AuthData
import com.example.myapplication.breeds.leaderboard.db.Result

fun NavGraphBuilder.profilescreen(
    route: String,
    onItemClick: () -> Unit,
    onBack: () -> Unit
) =composable(
    route = route
){
    val profileViewModel : ProfileViewModel = hiltViewModel<ProfileViewModel>()

    val state = profileViewModel.state.collectAsState()

    ProfileScreen(
        state = state.value,
        onItemClick = onItemClick,
        onBack = onBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileContract.ProfileState,
    onItemClick: () -> Unit,
    onBack: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    text = "Profil",
                    modifier = Modifier.padding(bottom = 16.dp)
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

        state.profileInfo?.let {
            ProfileInfo(it)
        }

        Button(onClick = onItemClick) {
            Text("Izmeni profil")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Najbolji rezultat: ${state.results.maxByOrNull { it.result }?.result ?: "Nema rezultata"}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.results) { result ->
                ResultItem(result = result)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }

}


@Composable
fun ProfileInfo(profileInfo: AuthData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Ime: ${profileInfo.firstName}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Prezime: ${profileInfo.lastName}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Nadimak: ${profileInfo.nickname}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Email: ${profileInfo.email}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ResultItem(result: Result) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Kategorija: ${result.category}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Nadimak: ${result.nickname}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Rezultat: ${result.result}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}