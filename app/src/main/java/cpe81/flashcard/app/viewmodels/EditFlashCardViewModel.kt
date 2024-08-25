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

    private var currentFlashCardId by mutableStateOf<Int?>(null)

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    fun updateAnswer(index: Int, newAnswer: String) {
        answers = answers.toMutableList().also { it[index] = newAnswer }
    }

    fun updateAnswers(newAnswers: List<String>) {
        answers = newAnswers.padEnd(4, "")
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

    fun setFlashCardData(flashCard: FlashCard) {
        if (flashCard.id != currentFlashCardId) {
            reset()
            question = flashCard.question
            updateAnswers(flashCard.answers)
            correctAnswerIndex = flashCard.correctAnswerIndex
            currentFlashCardId = flashCard.id
        }
    }

    fun reset() {
        question = ""
        answers = listOf("", "", "", "")
        correctAnswerIndex = 0
        currentFlashCardId = null
    }

    private fun List<String>.padEnd(length: Int, padding: String): List<String> {
        return this + List(maxOf(0, length - this.size)) { padding }
    }
}