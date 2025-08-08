package com.google.codelab.gamingzone.presentation.games.fear_zone

data class Tile(
    val id: Int,
    val row: Int,
    val col: Int,
    val type: TileType,
    val isVisible: Boolean = false,
    val isPlayerOn: Boolean = false
)



data class FearZoneState(
    val playerPos: Pair<Int, Int> = 0 to 0,
    val enemyPos: Pair<Int, Int> = 4 to 4,
    val lockTiles: Set<Pair<Int, Int>> = setOf(1 to 1, 2 to 3, 3 to 1),
    val unlockedLocks: Set<Pair<Int, Int>> = emptySet(),
    val safeTile: Pair<Int, Int>? = null,
    val fearLevel: Float = 0f,
    val turnCount: Int = 0,
    val isFlicker: Boolean = false,
    val isGameOver: Boolean = false,
    val isCompleted: Boolean = false
)

data class Player(val row: Int, val col: Int)

enum class TileType { NORMAL, SPIKE, SAFE, HIDDEN }
enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0)
}
