package com.google.codelab.gamingzone.domain.math_dungeon

enum class TileType {
    Trap, Empty, Exit, Potion
}

data class MathQuestion(
    val question: String,
    val answer: Int
)

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class Tile(
    val row: Int,
    val col: Int,
    val type: TileType,
    val question: MathQuestion? = null,
    val visited: Boolean = false
)

data class PlayerState(
    val position: Pair<Int, Int> = 0 to 0,
    val lives: Int = 3,
    val score: Int = 0
)


