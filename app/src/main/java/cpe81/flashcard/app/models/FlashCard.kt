package cpe81.flashcard.app.models

class FlashCard(
    val id: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
) : Identifiable {

    companion object {
        fun getFlashCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    1,
                    "What is the capital of France?",
                    listOf("Paris", "London", "Berlin", "Madrid"),
                    0
                ),
                FlashCard(
                    2,
                    "Who wrote 'To Kill a Mockingbird'?",
                    listOf("Harper Lee", "Mark Twain", "J.K. Rowling", "Ernest Hemingway"),
                    0
                ),
                FlashCard(
                    3,
                    "What is the chemical symbol for water?",
                    listOf("H2O", "O2", "CO2", "NaCl"),
                    0
                )
            )
        }
    }

    override fun getIdentifier(): Int {
        return id
    }
}
