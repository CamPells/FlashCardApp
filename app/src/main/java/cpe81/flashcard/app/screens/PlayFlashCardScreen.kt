package cpe81.flashcard.app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cpe81.flashcard.app.models.FlashCard
import cpe81.flashcard.app.viewmodels.FlashCardViewModel
import cpe81.flashcard.app.viewmodels.PlayFlashCardViewModel
import cpe81.flashcard.app.components.SoundPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayFlashCardScreen(
    navController: NavController,
    flashCardViewModel: FlashCardViewModel,
    playViewModel: PlayFlashCardViewModel
) {
    val context = LocalContext.current
    val flashCards by flashCardViewModel.flashCards.collectAsState()

    LaunchedEffect(Unit) {
        flashCardViewModel.getFlashCards()
    }

    LaunchedEffect(flashCards) {
        if (flashCards.isNotEmpty()) {
            playViewModel.loadFlashCards(flashCards)
        }
    }

    val currentCard = playViewModel.getCurrentFlashCard()

    if (playViewModel.isGameFinished) {
        GameSummaryScreen(
            score = playViewModel.score,
            totalQuestions = flashCards.size,
            wrongAnswers = playViewModel.wrongAnswers,
            elapsedTime = playViewModel.getFormattedTime(),
            onPlayAgain = { playViewModel.resetGame() },
            onBackToHome = { navController.popBackStack() }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Time: ${playViewModel.getFormattedTime()}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Progress: ${playViewModel.getProgress()}",
                style = MaterialTheme.typography.titleMedium
            )

            if (currentCard != null) {
                Text(
                    text = currentCard.question,
                    style = MaterialTheme.typography.headlineMedium
                )

                currentCard.answers.forEachIndexed { index, answer ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = playViewModel.selectedAnswerIndex == index,
                                onClick = { playViewModel.selectAnswer(index) }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = playViewModel.selectedAnswerIndex == index,
                            onClick = { playViewModel.selectAnswer(index) }
                        )
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        if (playViewModel.canSubmit()) {
                            val isCorrect = playViewModel.submitAnswer()
                            if (isCorrect) {
                                SoundPlayer.playCorrectSound(context)
                                Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}
@Composable
fun GameSummaryScreen(
    score: Int,
    totalQuestions: Int,
    wrongAnswers: List<FlashCard>,
    elapsedTime: String,
    onPlayAgain: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Game Summary",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Score: $score / $totalQuestions",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Time taken: $elapsedTime",
            style = MaterialTheme.typography.titleMedium
        )

        if (wrongAnswers.isNotEmpty()) {
            Text(
                text = "Questions you got wrong:",
                style = MaterialTheme.typography.titleMedium
            )

            wrongAnswers.forEach { flashCard ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Q: ${flashCard.question}")
                        Text(
                            text = "Correct A: ${flashCard.answers[flashCard.correctAnswerIndex]}",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Congratulations! You got all questions correct!",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onPlayAgain) {
                Text("Play Again")
            }
            Button(onClick = onBackToHome) {
                Text("Back to Home")
            }
        }
    }
}
