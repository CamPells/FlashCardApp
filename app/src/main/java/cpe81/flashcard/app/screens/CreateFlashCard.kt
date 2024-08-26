package cpe81.flashcard.app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
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
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
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
        if (answers.size < 4) {
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
                when {
                    question.isBlank() -> {
                        Toast.makeText(context, "Please enter a question", Toast.LENGTH_SHORT).show()
                    }
                    answers.any { it.isBlank() } -> {
                        Toast.makeText(context, "Please fill in all answers", Toast.LENGTH_SHORT).show()
                    }
                    correctAnswerIndex == null -> {
                        Toast.makeText(context, "Please select the correct answer", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        createFlashCardFn(question, answers, correctAnswerIndex)
                        Toast.makeText(context, "Flash card created successfully!", Toast.LENGTH_SHORT).show()
                        onQuestionChange("")
                        onAnswersChange(listOf("", "", "", "")) // Reset answers to 4 empty strings
                        onCorrectAnswerIndexChange(-1)
                        navController.navigate("Home")
                    }
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