package com.google.codelab.gamingzone.presentation.games.sudoku_screen

sealed class SudokuAction {
    data class SelectCell(val row: Int, val col: Int) : SudokuAction()
    data class EnterNumber(val number: Int) : SudokuAction()
    object UseHint : SudokuAction()
    object RestartGame : SudokuAction()
    data class SetDifficulty(val difficulty: Difficulty) : SudokuAction()
}



