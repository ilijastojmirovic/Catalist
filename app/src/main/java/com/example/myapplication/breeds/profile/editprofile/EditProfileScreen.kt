package com.example.myapplication.breeds.profile.editprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

fun NavGraphBuilder.editProfileScreen(
    route: String,
    onSaveSuccess: () -> Unit,
    onBack: () -> Unit
) = composable(
    route = route
) {
    val editProfileViewModel: EditProfileViewModel = hiltViewModel()

    val state = editProfileViewModel.state.collectAsState()


    EditProfileScreen(
        state = state.value,
        eventPublisher = {
            editProfileViewModel.setEvent(it)
        },
        onSaveSuccess = onSaveSuccess,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    state: EditProfileContract.EditProfileState,
    eventPublisher: (EditProfileContract.EditProfileEvent) -> Unit,
    onSaveSuccess: () -> Unit,
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                text = "Izmeni profil",
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = state.firstName,
            onValueChange = { eventPublisher(EditProfileContract.EditProfileEvent.UpdateFirstName(it)) },
            label = { Text("Ime") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.lastName,
            onValueChange = { eventPublisher(EditProfileContract.EditProfileEvent.UpdateLastName(it)) },
            label = { Text("Prezime") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.nickname,
            onValueChange = { eventPublisher(EditProfileContract.EditProfileEvent.UpdateNickname(it)) },
            label = { Text("Nadimak") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { eventPublisher(EditProfileContract.EditProfileEvent.UpdateEmail(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                eventPublisher(EditProfileContract.EditProfileEvent.SaveProfile)
                onSaveSuccess()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sačuvaj")
        }
    }
}