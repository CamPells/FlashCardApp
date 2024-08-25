package cpe81.flashcard.app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cpe81.flashcard.app.models.FlashCard
import cpe81.flashcard.app.viewmodels.EditFlashCardViewModel
import cpe81.flashcard.app.viewmodels.FlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFlashCard(
    flashCardId: String,
    editFlashCardViewModel: EditFlashCardViewModel,
    flashCardViewModel: FlashCardViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val selectedFlashCardState by flashCardViewModel.selectedFlashCard.collectAsState(null)
    val flashCard: FlashCard? = selectedFlashCardState
    val scrollState = rememberScrollState()

    LaunchedEffect(flashCardId) {
        flashCardViewModel.getFlashCardById(flashCardId.toIntOrNull())
    }

    LaunchedEffect(flashCard) {
        flashCard?.let { editFlashCardViewModel.setFlashCardData(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp) // Add padding at the bottom for the button
        ) {
            OutlinedTextField(
                value = editFlashCardViewModel.question,
                onValueChange = { editFlashCardViewModel.updateQuestion(it) },
                label = { Text("Question") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            editFlashCardViewModel.answers.forEachIndexed { index, answer ->
                OutlinedTextField(
                    value = answer,
                    onValueChange = { editFlashCardViewModel.updateAnswer(index, it) },
                    label = { Text("Answer ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            Text("Select the correct answer:", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            editFlashCardViewModel.answers.forEachIndexed { index, _ ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = editFlashCardViewModel.correctAnswerIndex == index,
                        onClick = { editFlashCardViewModel.updateCorrectAnswerIndex(index) }
                    )
                    Text("Answer ${index + 1}", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        Button(
            onClick = {
                if (editFlashCardViewModel.isValid()) {
                    flashCardViewModel.editFlashCardById(
                        flashCardId.toIntOrNull(),
                        FlashCard(
                            flashCardId.toInt(),
                            editFlashCardViewModel.question,
                            editFlashCardViewModel.answers.filter { it.isNotBlank() },
                            editFlashCardViewModel.correctAnswerIndex
                        )
                    )
                    Toast.makeText(context, "Flash card updated successfully", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Please fill all fields and select a correct answer", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text("Save Changes")
        }
    }
}