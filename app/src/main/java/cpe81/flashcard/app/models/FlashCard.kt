package cpe81.flashcard.app.models

class FlashCard(
    val id: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val timestamp: Long,
    val isArchived: Boolean = false
) : Identifiable {

    companion object {
        fun getSampleFlashCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    1,
                    "What is the capital of France?",
                    listOf("Paris", "London", "Berlin", "Madrid"),
                    0,
                    1637653200000
                ),
                FlashCard(
                    2,
                    "Who wrote 'To Kill a Mockingbird'?",
                    listOf("Harper Lee", "Mark Twain", "J.K. Rowling", "Ernest Hemingway"),
                    0,
                    1637725200000
                ),
                FlashCard(
                    3,
                    "What is the chemical symbol for water?",
                    listOf("H2O", "O2", "CO2", "NaCl"),
                    0,
                    1637811600000
                )
            )
        }
    }

    override fun getIdentifier(): Int {
        return id
    }
}

