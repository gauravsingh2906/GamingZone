package com.google.codelab.gamingzone.presentation.games


data class Tile(
    val row: Int,
    val col: Int,
    val isBlocked: Boolean = false,
    val isBot: Boolean = false
)

data class TrapTheBotState(
    val gridSize: Int = 9,
    val tiles: List<List<Tile>>,
    val botPosition: Pair<Int, Int>,
    val isGameOver: Boolean = false,
    val playerWon: Boolean = false
)

sealed class TrapTheBotEvent {
    data class BlockTile(val row: Int, val col: Int) : TrapTheBotEvent()
    object Restart : TrapTheBotEvent()
}

fun createInitialState(): TrapTheBotState {
    val size = 9
    val center = size / 2
    val tiles = List(size) { row ->
        List(size) { col ->
            Tile(row = row, col = col, isBot = row == center && col == center)
        }
    }
    return TrapTheBotState(
        tiles = tiles,
        botPosition = center to center
    )
}


