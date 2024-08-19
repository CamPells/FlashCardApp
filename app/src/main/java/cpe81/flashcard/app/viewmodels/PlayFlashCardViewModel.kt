package cpe81.flashcard.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cpe81.flashcard.app.models.FlashCard

class PlayFlashCardViewModel : ViewModel() {
    private var flashCards: List<FlashCard> = emptyList()
    var currentIndex by mutableStateOf(0)
        private set
    var selectedAnswerIndex by mutableStateOf<Int?>(null)
        private set
    var score by mutableStateOf(0)
        private set
    var isGameFinished by mutableStateOf(false)
        private set

    fun loadFlashCards(cards: List<FlashCard>) {
        flashCards = cards.shuffled()
        currentIndex = 0
        score = 0
        isGameFinished = false
    }

    fun getCurrentFlashCard(): FlashCard? = flashCards.getOrNull(currentIndex)

    fun selectAnswer(index: Int) {
        selectedAnswerIndex = index
    }

    fun submitAnswer(): Boolean {
        val currentCard = getCurrentFlashCard() ?: return false
        val isCorrect = selectedAnswerIndex == currentCard.correctAnswerIndex
        if (isCorrect) score++

        if (currentIndex < flashCards.size - 1) {
            currentIndex++
            selectedAnswerIndex = null
        } else {
            isGameFinished = true
        }

        return isCorrect
    }

    fun getProgress(): String = "${currentIndex + 1}/${flashCards.size}"

    fun canSubmit(): Boolean = selectedAnswerIndex != null
}