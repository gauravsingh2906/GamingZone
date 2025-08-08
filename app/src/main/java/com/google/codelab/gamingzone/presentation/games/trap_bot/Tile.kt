package com.google.codelab.gamingzone.presentation.games.trap_bot

import com.google.codelab.gamingzone.presentation.games.chess_screen3.Position
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Cell
import kotlinx.serialization.Serializable
import kotlin.random.Random


data class Tile(
    val row: Int,
    val col: Int,
    val isBlocked: Boolean = false,
    val isBot: Boolean = false
)

//data class TrapTheBotState(
//    val gridSize: Int = 9,
//    val tiles: List<List<Tile>> = emptyList(),
//    val botPosition: Pair<Int, Int> = 4 to 4,
//    val playerTurns: Int = 0,
//    val isGameOver: Boolean = false,
//    val playerWon: Boolean = false
//)



data class Positioner(
    val row: Int,
    val col: Int
)


data class TrapBotGameState(
    val grid: List<List<Tile>>,
    val botPosition: Positioner,
    val playerMoves: Int,
    val gameResult: GameResult?,
    val difficulty: DifficultyLevel,
    val currentPlayerMoveCount: Int = 0 // NEW FIELD
) {
    companion object {
        fun initial(difficulty: DifficultyLevel): TrapBotGameState {
            val botPos = when (difficulty) {
                DifficultyLevel.EASY -> 3 to 3
                DifficultyLevel.MEDIUM -> 4 to 4
                DifficultyLevel.HARD -> 4 to 4
            }

            return TrapBotGameState(
                grid = generateInitialGrid(
                    rows = 9, cols = 9, botPosition = botPos
                ),
                botPosition = Positioner(botPos.first, botPos.second),
                playerMoves = 0,
                gameResult = null,
                difficulty = difficulty
            )
        }
    }

}




fun generateInitialGrid(
    rows: Int,
    cols: Int,
    botPosition: Pair<Int, Int>
): List<List<Tile>> {
    return List(rows) { row ->
        List(cols) { col ->
            Tile(
                row = row,
                col = col,
                isBlocked = false,
                isBot = (row == botPosition.first && col == botPosition.second)
            )
        }
    }
}



sealed class GameResult {
    data object InProgress : GameResult()
    data object Win : GameResult()
    data object Lose : GameResult()
}



sealed class TrapTheBotEvent {
    data class BlockTile(val row: Int, val col: Int) : TrapTheBotEvent()
    object Restart : TrapTheBotEvent()
    data class SetDifficulty(val difficulty: DifficultyLevel) : TrapTheBotEvent()
}

@Serializable
enum class DifficultyLevel(val playerMovesPerTurn: Int, val botMovesPerTurn: Int) {
    EASY(playerMovesPerTurn = 2, botMovesPerTurn = 1),
    MEDIUM(playerMovesPerTurn = 1, botMovesPerTurn = 1),
    HARD(playerMovesPerTurn = 1, botMovesPerTurn = 1) // You can tweak HARD later
}



//
//fun createInitialState(): TrapBotGameState {
//    val size = 9
//    val center = size / 2
//    val tiles = List(size) { row ->
//        List(size) { col ->
//            Tile(row = row, col = col, isBot = row == center && col == center)
//        }
//    }
//    return TrapBotGameState(
//        tiles = tiles,
//        botPosition = center to center,
//    )
//}


