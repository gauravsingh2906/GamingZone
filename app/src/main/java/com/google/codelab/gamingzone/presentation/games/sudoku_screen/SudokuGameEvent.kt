package com.google.codelab.gamingzone.presentation.games.sudoku_screen

sealed class SudokuGameEvent {
    object GameOver : SudokuGameEvent()
    object PuzzleSolved : SudokuGameEvent()
}