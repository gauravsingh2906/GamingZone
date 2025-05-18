package com.google.codelab.gamingzone.presentation.games.sudoku_screen

data class SudokuState(
    val board: List<List<Cell>> = emptyList(),
    val originalBoard: List<List<Cell>> = emptyList(),
    val solutionBoard: List<List<Cell>> = emptyList(),
    val selectedCell: Pair<Int, Int>? = null,
    val selectedNumber: Int? = null,
    val invalidCells: Set<Pair<Int, Int>> = emptySet(),
    val hintsUsed: Int=0,
    val mistakes: Int = 0,
    val isGameOver: Boolean = false,
    val isGameWon: Boolean = false,
    val elapsedTime: Int = 0,
    val difficulty: Difficulty? = Difficulty.EASY
)








