package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import kotlinx.serialization.Serializable

data class Cell(
    val row: Int,
    val col: Int,
    val value: Int,
    val isFixed: Boolean,
    val isSelected: Boolean = false,     // To highlight selection
    val isError: Boolean = false,        // To show mistake coloring
    val isHint: Boolean = false          // To animate or color hints
)



@Serializable
enum class Difficulty(val blanks: IntRange) {
    EASY(blanks = 36..40),
    MEDIUM(41..46),
    HARD(47..54)
}
