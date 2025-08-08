package com.google.codelab.gamingzone.presentation.games.glitch_escape

import android.util.Log

data class LevelData(
    val levelNumber: Int,
    val gridSize: Int,
    val playerStart: Pair<Int, Int>,
    val portal: Pair<Int, Int>,
    val glitchTiles: List<Pair<Int, Int>>,
    val enemyPaths: List<List<Pair<Int, Int>>>
)

enum class GlitchDifficulty(val glitchBoost: Int, val enemyBoost: Int) {
    EASY(glitchBoost = 0, enemyBoost = 0),
    HARD(glitchBoost = 2, enemyBoost = 1),
    IMPOSSIBLE(glitchBoost = 4, enemyBoost = 2)
}

fun generateLevel(levelNumber: Int, difficulty: GlitchDifficulty): LevelData {
    val gridSize = (6 + levelNumber.coerceAtMost(6))
    val playerStart = 0 to 0
    val portal = (gridSize - 1) to (gridSize - 1)

    Log.d("LevelData", "Generating level $levelNumber with difficulty $difficulty")

    val allPositions = (0 until gridSize).flatMap { x ->
        (0 until gridSize).map { y -> x to y }
    }.filterNot { it == playerStart || it == portal }

    val glitchCount = 4 + (levelNumber % 5) + difficulty.glitchBoost
    val glitchTiles = allPositions.shuffled().take(glitchCount)

    val enemyCount = (1 + (levelNumber / 3) + difficulty.enemyBoost).coerceAtMost(4)
    val enemyPaths = List(enemyCount) {
        val pathLength = 3 + (levelNumber % 4)
        val startX = (1 until gridSize - 1).random()
        val path = List(pathLength) { step ->
            val y = (step % (gridSize - 1)).coerceIn(1, gridSize - 2)
            startX to y
        }
        path + path.first()
    }

    return LevelData(
        levelNumber = levelNumber,
        gridSize = gridSize,
        playerStart = playerStart,
        portal = portal,
        glitchTiles = glitchTiles,
        enemyPaths = enemyPaths
    )
}
