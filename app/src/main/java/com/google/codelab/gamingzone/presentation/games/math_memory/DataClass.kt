package com.google.codelab.gamingzone.presentation.games.math_memory

import android.util.Log
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.Shape
import com.google.codelab.gamingzone.R
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.Default


// Enum for operation types
enum class Op { ADD, SUB, MUL, DIV }

data class MemoryCard(
    val op: Op,
    val value: Int
)

//data class MemoryLevel(
//    val number: Int,
//    val cards: List<MemoryCard>,
//    val start: Int
//) {
//    val correctAnswer: Int
//        get() = cards.fold(start) { acc, card ->
//            when (card.op) {
//                Op.ADD -> acc + card.value
//                Op.SUB -> acc - card.value
//                Op.MUL -> acc * card.value
//                Op.DIV -> if (card.value != 0) acc / card.value else acc
//            }
//        }
//}

data class MemoryLevel(
    val number: Int,
    val cards: List<MemoryCard>,
    val start: Int
) {
    val correctAnswer: Int
        get() {
            var acc = start
            cards.forEachIndexed { idx, card ->
                val prev = acc
                acc = when (card.op) {
                    Op.ADD -> acc + card.value
                    Op.SUB -> acc - card.value
                    Op.MUL -> acc * card.value
                    Op.DIV -> if (card.value != 0) acc / card.value else acc
                }
                Log.d("MathMemoryDebug", "Step ${idx + 1}: ${card.op} ${card.value} ($prev ${symbol(card.op)} ${card.value}) = $acc")
            }
            return acc
        }

}

private fun symbol(op: Op): String = when (op) {
    Op.ADD -> "+"
    Op.SUB -> "-"
    Op.MUL -> "×"
    Op.DIV -> "÷"
}




// Game state for Compose
data class MathMemoryGameState(
    val level: MemoryLevel,
    val isShowCards: Boolean = true,
    val userInput: String = "",
    val showResult: Boolean = false,
    val isCorrect: Boolean = false
)

data class MathMemoryGameUIState(
    val game: MathMemoryGameState,
    val theme: ThemeState = ThemeState()
)



// UI/user actions consumed by the ViewModel
sealed class MathMemoryAction {
    object RevealCards : MathMemoryAction()
    data class InputChanged(val value: String) : MathMemoryAction()
    object SubmitAnswer : MathMemoryAction()
    object NextLevel : MathMemoryAction()
    object ResetGame : MathMemoryAction()

    object HideCards : MathMemoryAction()

    data class SelectTheme(val theme: GameTheme) : MathMemoryAction()
    object UnlockNextTheme : MathMemoryAction() // Example action
}




enum class Theme(val displayName: String, val bgColor: Color) {
    DEFAULT("Classic", Color(0xFF1B1B1B)),
    FOREST("Forest", Color(0xFF2E7D32)),
    AQUA("Aqua", Color(0xFF64B5F6)),
    LEMON("Lemon", Color(0xFFFFF176)),
}

data class ThemeState(
    val unlockedThemes: Set<String> = setOf(Default.first().name), // store theme names
    val selectedTheme: GameTheme = Default.first() // store full GameTheme object
)


//data class MathMemoryUiState(
//    val gameState: MathMemoryGameState = MathMemoryGameState(
//        level = MemoryLevel(
//            number = 1,
//            cards = emptyList(),
//            start = 0
//        )
//    ),
//    val currentTheme: GameTheme = GameTheme(
//        name = "Nature",
//        backgroundImage = R.drawable.theme1,
//        textColor = Color.White,
//        buttonColor = Color(0xFF388E3C),
//        buttonTextColor = Color.White
//    ),
//    val isLoading: Boolean = false,
//    val isGameOver: Boolean = false,
//    val unlockedThemes: Set<String> = setOf("Nature") // Default unlocked theme
//)



data class GameTheme(
    val name: String,
    val backgroundImage: Int, // Resource ID of drawable
    val textColor: Color,
    val buttonColor: Color,
    val buttonTextColor: Color
)

data class AnswerOption(
    val value: Int,
    val isCorrect: Boolean
)




data class CardStyle(
    val bgColor: Color,
    val shape: Shape,
    val textColor: Color
)




