package com.google.codelab.gamingzone.presentation.games.math_path


data class MathPathState(
    val grid: List<List<MathTile>> = emptyList(),
    val selectedTiles: List<MathTile> = emptyList(),
    val streak: Int=0,
    val targetSum: Int = 0,
    val score: Int = 0,
    val movesLeft: Int = 30,
    val isGameOver: Boolean = false,
    val level: Int = 1,
    val motivationalText: String = "",
    val confettiTrigger: Boolean = false
)





enum class DifficultyMath(val gridSize: Int, val targetRange: IntRange, val maxMoves: Int) {
    EASY(gridSize = 4, targetRange = 10..30, maxMoves = 10),
    MEDIUM(gridSize = 5, targetRange = 30..60, maxMoves = 12),
    HARD(gridSize = 6, targetRange = 50..90, maxMoves = 14)
}

