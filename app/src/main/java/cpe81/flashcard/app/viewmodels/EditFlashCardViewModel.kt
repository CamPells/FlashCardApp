package cpe81.flashcard.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cpe81.flashcard.app.models.FlashCard

class EditFlashCardViewModel : ViewModel() {
    var question by mutableStateOf("")
        private set

    var answers by mutableStateOf(listOf("", "", "", ""))
        private set

    var correctAnswerIndex by mutableStateOf(0)
        private set

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    fun updateAnswer(index: Int, newAnswer: String) {
        answers = answers.toMutableList().also { it[index] = newAnswer }
    }

    fun updateCorrectAnswerIndex(newIndex: Int) {
        correctAnswerIndex = newIndex
    }

    fun isValid(): Boolean {
        return question.isNotBlank() &&
                answers.count { it.isNotBlank() } >= 2 &&
                correctAnswerIndex in answers.indices &&
                answers[correctAnswerIndex].isNotBlank()
    }

    fun setDefaultValues(selectedFlashCard: FlashCard?) {
        selectedFlashCard?.let {
            question = it.question
            answers = it.answers + List(4 - it.answers.size) { "" }
            correctAnswerIndex = it.correctAnswerIndex
        }
    }
}