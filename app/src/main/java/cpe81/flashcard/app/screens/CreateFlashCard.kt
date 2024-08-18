package cpe81.flashcard.app.screens

import android.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashCard(
    navController: NavController,
    question: String,
    onQuestionChange: (String) -> Unit,
    answers: List<String>,
    onAnswersChange: (List<String>) -> Unit,
    correctAnswerIndex: Int?,
    onCorrectAnswerIndexChange: (Int) -> Unit,
    createFlashCardFn: (String, List<String>, Int) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = question,
            onValueChange = onQuestionChange,
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        answers.forEachIndexed { index, answer ->
            OutlinedTextField(
                value = answer,
                onValueChange = { newAnswer ->
                    val updatedAnswers = answers.toMutableList().apply {
                        this[index] = newAnswer
                    }
                    onAnswersChange(updatedAnswers)
                },
                label = { Text("Answer ${index + 1}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                onAnswersChange(answers + "")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Add Answer")
        }

        Text("Select the Correct Answer:")
        answers.forEachIndexed { index, _ ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = correctAnswerIndex == index,
                    onClick = { onCorrectAnswerIndexChange(index) }
                )
                Text("Answer ${index + 1}")
            }
        }

        Button(
            onClick = {
                if (question.isNotBlank() && answers.all { it.isNotBlank() } && answers.size >= 2 && correctAnswerIndex != null) {
                    createFlashCardFn(question, answers, correctAnswerIndex)
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Created flash card!")
                        .setCancelable(false)
                        .setPositiveButton("Ok") { dialog, _ ->
                            onQuestionChange("")
                            onAnswersChange(listOf("", ""))
                            onCorrectAnswerIndexChange(-1)
                            navController.navigate("Home")
                        }
                        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}