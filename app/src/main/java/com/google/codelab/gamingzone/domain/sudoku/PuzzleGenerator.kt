package com.google.codelab.gamingzone.domain.sudoku

import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty


interface PuzzleGenerator {
    fun generate(difficulty: Difficulty): Pair<List<List<Int>>, List<List<Int>>>
}