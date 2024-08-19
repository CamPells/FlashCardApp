package cpe81.flashcard.app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cpe81.flashcard.app.viewmodels.FlashCardViewModel
import cpe81.flashcard.app.viewmodels.PlayFlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayFlashCardScreen(
    navController: NavController,
    flashCardViewModel: FlashCardViewModel,
    playViewModel: PlayFlashCardViewModel
) {
    val context = LocalContext.current
    val flashCards by flashCardViewModel.flashCards.collectAsState()

    LaunchedEffect(flashCards) {
        if (flashCards.isNotEmpty()) {
            playViewModel.loadFlashCards(flashCards)
        }
    }

    val currentCard = playViewModel.getCurrentFlashCard()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        val message = if (isCorrect) "Correct!" else "Incorrect."
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                        if (playViewModel.isGameFinished) {
                            Toast.makeText(
                                context,
                                "Game Over! Your score: ${playViewModel.score}/${flashCards.size}",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Submit")
            }
        } else {
            Text("No flash cards available.")
        }
    }
}