package cpe81.flashcard.app.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cpe81.flashcard.app.models.FlashCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.logger.Logger


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
    private var isInitialized by mutableStateOf(false)
    var wrongAnswers by mutableStateOf<List<FlashCard>>(emptyList())
        private set
    var currentStreak by mutableStateOf(0)
        private set
    var bestStreak by mutableStateOf(0)
        private set
    var shuffleFlag by mutableStateOf(false)
    var shuffle by mutableStateOf(false)

    var elapsedTime by mutableStateOf(0L)
        private set
    private var timerJob: Job? = null

    fun loadFlashCards(cards: List<FlashCard>) {
        // stupid t
        if (shuffleFlag) {
            flashCards = if (shuffle) {
                cards.shuffled()
            } else {
                cards.sortedBy { it.id }
            }
            shuffleFlag = false
        }


        if (!isInitialized) {
            flashCards = flashCards.ifEmpty {
                if (shuffle) cards.shuffled() else cards.sortedBy { it.id }
            }
            isInitialized = true
        }
    }
    fun setShuffledFlag() {
        shuffleFlag = true
    }
    fun setshuffle(){shuffle = true}



    fun getCurrentFlashCard(): FlashCard? = flashCards.getOrNull(currentIndex)

    fun selectAnswer(index: Int) {
        selectedAnswerIndex = index
    }

    fun submitAnswer(): Boolean {
        val currentCard = getCurrentFlashCard() ?: return false
        val isCorrect = selectedAnswerIndex == currentCard.correctAnswerIndex
        if (isCorrect) {
            score++
            currentStreak++
            if (currentStreak > bestStreak) {
                bestStreak = currentStreak}
        } else {
            wrongAnswers = wrongAnswers + currentCard
        }

        if (currentIndex < flashCards.size - 1) {
            currentIndex++
            selectedAnswerIndex = null
        } else {
            finishGame()
            currentStreak = 0
        }

        return isCorrect
    }

    fun getProgress(): String = "${currentIndex + 1}/${flashCards.size}"

    fun canSubmit(): Boolean = selectedAnswerIndex != null

    fun resetGame() {
        currentIndex = 0
        selectedAnswerIndex = null
        score = 0
        isGameFinished = false
        wrongAnswers = emptyList()
        elapsedTime = 0L
        startTimer()
        currentStreak = 0
        shuffleFlag = false
        shuffle = false

    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                elapsedTime += 1000
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun finishGame() {
        isGameFinished = true
        stopTimer()
    }

    fun getFormattedTime(): String {
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }

}