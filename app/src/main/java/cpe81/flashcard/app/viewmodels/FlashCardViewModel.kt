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


    fun getFlashCards() = viewModelScope.launch {
        flashCardStorage.getAll().catch { Log.e("FLASH_CARD_VIEW_MODEL", it.toString()) }
            .collect { _flashCards.emit(it) }
    }

    fun loadDefaultNotesIfNoneExist() = viewModelScope.launch {
        val currentNotes = flashCardStorage.getAll().first()
        if (currentNotes.isEmpty()) {
            Log.d("NOTE_VIEW_MODEL", "Inserting default notes...")
            flashCardStorage.insertAll(FlashCard.getFlashCards())
                .catch { Log.w("NOTE_VIEW_MODEL", "Could not insert default notes") }.collect {
                    Log.d("NOTE_VIEW_MODEL", "Default notes inserted successfully")
                    _flashCards.emit(FlashCard.getFlashCards())
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
            try {
                _selectedFlashCard.value = flashCardStorage.get { it.id == flashCardId }.first()
            } catch (e: NoSuchElementException) {
                Log.e("FLASH_CARD_VIEW_MODEL", "Flash card not found: $flashCardId")
                _selectedFlashCard.value = null
            }
        } else {
            _selectedFlashCard.value = null
        }
    }

    fun deleteFlashCardById(flashCardId: Int?) = viewModelScope.launch {
        flashCardId?.let { id ->
            flashCardStorage.delete(id).collect { result ->
                Log.d("FLASH_CARD_VIEW_MODEL", "Delete result for ID $id: $result")
                getFlashCards()
            }
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
