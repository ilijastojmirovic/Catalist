package com.example.myapplication.breeds.localaccount

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import com.example.myapplication.breeds.auth.AuthData
import com.example.myapplication.breeds.localaccount.LocalAccountContract.LocalAccountEvent


fun NavGraphBuilder.createLocalAccount(
    route: String,
    arguments: List<NamedNavArgument>,
    onItemClick: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) {navBackStackEntry ->

    val localAccountViewModel: LocalAccountViewModel = hiltViewModel<LocalAccountViewModel>(navBackStackEntry)

    val state = localAccountViewModel.state.collectAsState()

    LocalAccountScreen(
        state = state.value,
        eventPublisher = {
            localAccountViewModel.setEvent(it)
        },
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalAccountScreen(
    state: LocalAccountContract.LocalAccountState,
    eventPublisher: (uiEvent: LocalAccountEvent) -> Unit,
    onItemClick: () -> Unit
){

//    LaunchedEffect(Unit) {
//        if (state.dataStoreHasContent) {
//            Log.e("DATASTORE", state.nickname)
//            onItemClick()
//        }
//    }

//    if (state.loading) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = state.firstName,
                onValueChange = { eventPublisher(LocalAccountEvent.UpdateFirstName(it)) },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.lastName,
                onValueChange = { eventPublisher(LocalAccountEvent.UpdateLastName(it)) },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.nickname,
                onValueChange = { eventPublisher(LocalAccountEvent.UpdateNickname(it)) },
                label = { Text("Nickname") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.email,
                onValueChange = { eventPublisher(LocalAccountEvent.UpdateEmail(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val localAccount = AuthData(
                        firstName = state.firstName,
                        lastName = state.lastName,
                        nickname = state.nickname,
                        email = state.email
                    )
                    eventPublisher(LocalAccountEvent.CreateLocalAccount(localAccount))
                    onItemClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kreiraj nalog")
            }
        }
    //}



}