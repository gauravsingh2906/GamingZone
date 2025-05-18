package com.google.codelab.gamingzone.presentation.games.color_race_screen2

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import kotlinx.serialization.Serializable


data class ColorRaceState(
    val level: Int = 1,
    val sequence: List<ColorOption> = emptyList(),
    val userInput: List<ColorOption> = emptyList(),
    val isShowingSequence: Boolean = false,
    val currentBlinkIndex: Int = -1,
    val isGameOver: Boolean = false,
    val showGameOverDialog: Boolean = false
)


enum class ColorOption(val color: Color) {
    BLUE(Color.Blue),
    RED(Color.Red),
    GREEN(Color.Green),
    YELLOW(Color.Yellow);

}






