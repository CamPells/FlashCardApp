package cpe81.flashcard.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreateFlashCardViewModel : ViewModel() {
    var question by mutableStateOf("")
        private set

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    var answers by mutableStateOf(listOf("", ""))
        private set

    fun updateAnswers(newAnswers: List<String>) {
        answers = newAnswers
    }

    var correctAnswerIndex by mutableStateOf<Int?>(null)
        private set

    fun updateCorrectAnswerIndex(newIndex: Int) {
        correctAnswerIndex = newIndex
    }

    fun addAnswer() {
        answers = answers + ""
    }

    fun resetForm() {
        question = ""
        answers = listOf("", "")
        correctAnswerIndex = null
    }
}