package com.example.myapplication.breeds.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import com.example.myapplication.breeds.localaccount.LocalAccountContract
import com.example.myapplication.breeds.localaccount.LocalAccountViewModel
import kotlinx.coroutines.delay

fun NavGraphBuilder.quiz(
    route: String,
    onItemClick: () -> Unit,
) = composable(
    route = route,

) { navBackStackEntry ->

    val quizViewModel: QuizViewModel = hiltViewModel<QuizViewModel>(navBackStackEntry)

    val state = quizViewModel.state.collectAsState()

    QuizScreen(
        state = state.value,
        eventPublisher = {
            quizViewModel.setEvent(it)
        },
        onItemClick = onItemClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    state: QuizContract.QuizState,
    eventPublisher: (uiEvent: QuizContract.QuizEvent) -> Unit,
    onItemClick: () -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else if (state.quizEnded) {



        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Kviz je gotov. Vas rezultat je: ${state.finalScore}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = onItemClick,
                        modifier = Modifier
//                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Zavrsi kviz")
                    }
                    Button(
                        onClick = { eventPublisher(QuizContract.QuizEvent.ShowPublishDialog) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(text = "Objavi rezultat")
                    }
                }
            }
        }
    } else if (state.questions.isNotEmpty()) {
        BackHandler {

        }
        val currentQuestion = state.questions[state.currentQuestionIndex]
        val questionVisible = remember { mutableStateOf(true) }

        // Reset visibility when question changes
        LaunchedEffect(currentQuestion) {
            questionVisible.value = false
            delay(500) // delay for exit animation
            questionVisible.value = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Preostalo vreme: ${state.timeLeft / 60}:${state.timeLeft % 60}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            AnimatedVisibility(
                visible = questionVisible.value,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { 300 }, animationSpec = tween(1000)),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -600 }, animationSpec = tween(1000))
//                enter = fadeIn() + slideInVertically(),
//                exit = fadeOut() + slideOutVertically()
            ) {
                Column{
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Image(
                        painter = rememberImagePainter(currentQuestion.imageUrl),
                        contentDescription = "Cat Image",
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    currentQuestion.options.forEach { option ->
                        Button(
                            onClick = { eventPublisher(QuizContract.QuizEvent.AnswerQuestion(option)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                        ) {
                            Text(option)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { eventPublisher(QuizContract.QuizEvent.ShowCancelDialog) },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Prekini kviz")
                    }
                }
            }

        }

    }

    if (state.showCancelDialog) {
        AlertDialog(
            onDismissRequest = { eventPublisher(QuizContract.QuizEvent.DismissCancelDialog) },
            title = { Text(text = "Prekini kviz") },
            text = { Text(text = "Da li ste sigurni da želite da prekinete kviz?") },
            confirmButton = {
                Button(onClick = { eventPublisher(QuizContract.QuizEvent.CancelQuiz) }) {
                    Text("Prekini kviz")
                }
            },
            dismissButton = {
                Button(onClick = { eventPublisher(QuizContract.QuizEvent.DismissCancelDialog) }) {
                    Text("Nastavi kviz")
                }
            }
        )
    }

    if (state.showPublishDialog) {
        AlertDialog(
            onDismissRequest = { eventPublisher(QuizContract.QuizEvent.DismissPublishDialog) },
            title = { Text(text = "Objavi rezultat") },
            text = { Text(text = "Da li ste sigurni da želite da objavite rezultat?") },
            confirmButton = {
                Button(onClick = { eventPublisher(QuizContract.QuizEvent.PublishResult) }) {
                    Text("Objavi")
                }
            },
            dismissButton = {
                Button(onClick = { eventPublisher(QuizContract.QuizEvent.DismissPublishDialog) }) {
                    Text("Odustani")
                }
            }
        )
    }
}