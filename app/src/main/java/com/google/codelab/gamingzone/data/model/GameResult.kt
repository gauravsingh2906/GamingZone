package com.google.codelab.gamingzone.data.model

import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty

data class GameResult(
    val gameName: String,
    val difficulty: Difficulty?,
    val isWin: Boolean?,
    val isDraw: Boolean = false,
    val xpEarned: Int
)
