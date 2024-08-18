package cpe81.flashcard.app.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cpe81.flashcard.app.datastore.Storage
import cpe81.flashcard.app.models.FlashCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class FlashCardViewModel(
    private val flashCardStorage: Storage<FlashCard>
) : ViewModel() {

    private val _flashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    val flashCards: StateFlow<List<FlashCard>> get() = _flashCards

    private val _selectedFlashCard = MutableStateFlow<FlashCard?>(null)
    val selectedFlashCard: StateFlow<FlashCard?> get() = _selectedFlashCard

    init {
        loadDefaultFlashCardsIfNoneExist()
    }

    fun getFlashCards() = viewModelScope.launch {
        flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }
            .collect { _flashCards.emit(it) }
    }

    private fun loadDefaultFlashCardsIfNoneExist() = viewModelScope.launch {
        val currentFlashCards = flashCardStorage.getAll().first()
        if (currentFlashCards.isEmpty()) {
            Log.d("FLASH_CARD_VIEW_MODEL", "Inserting default flash cards...")
            val defaultFlashCards = listOf(
                FlashCard(
                    id = Random.nextInt(0, Int.MAX_VALUE),
                    question = "What is the capital of France?",
                    answers = listOf("Paris", "London", "Berlin", "Madrid"),
                    correctAnswerIndex = 0
                ),
                FlashCard(
                    id = Random.nextInt(0, Int.MAX_VALUE),
                    question = "Who wrote 'To Kill a Mockingbird'?",
                    answers = listOf("Harper Lee", "Mark Twain", "J.K. Rowling", "Ernest Hemingway"),
                    correctAnswerIndex = 0
                )
            )
            flashCardStorage.insertAll(defaultFlashCards)
                .catch { Log.w("FLASH_CARD_VIEW_MODEL", "Could not insert default flash cards") }
                .collect {
                    Log.d("FLASH_CARD_VIEW_MODEL", "Default flash cards inserted successfully")
                    _flashCards.emit(defaultFlashCards)
                }
        }
    }

    fun createFlashCard(question: String, answers: List<String>, correctAnswerIndex: Int) = viewModelScope.launch {
        val flashCard = FlashCard(
            id = Random.nextInt(0, Int.MAX_VALUE),
            question = question,
            answers = answers,
            correctAnswerIndex = correctAnswerIndex
        )
        flashCardStorage.insert(flashCard).catch { Log.e("FLASH_CARD_VIEW_MODEL", "Could not insert flash card") }
            .collect()
        flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }
            .collect { _flashCards.emit(it) }
    }

    fun getFlashCardById(flashCardId: Int?) = viewModelScope.launch {
        if (flashCardId != null) {
            _selectedFlashCard.value = flashCardStorage.get { it.id == flashCardId }.first()
        } else {
            _selectedFlashCard.value = null
        }
    }

    fun deleteFlashCardById(flashCardId: Int?) = viewModelScope.launch {
        Log.d("FLASH_CARD_VIEW_MODEL", "Deleting flash card: $flashCardId")
        if (flashCardId != null) {
            flashCardStorage.delete(flashCardId)
            flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }
                .collect { _flashCards.emit(it) }
        }
    }

    fun editFlashCardById(flashCardId: Int?, flashCard: FlashCard) = viewModelScope.launch {
        Log.d("FLASH_CARD_VIEW_MODEL", "Editing flash card: $flashCardId")
        if (flashCardId != null) {
            flashCardStorage.edit(flashCardId, flashCard)
            flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }
                .collect { _flashCards.emit(it) }
        }
    }
}
