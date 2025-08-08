package com.google.codelab.gamingzone.presentation.games.trap_bot

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TrapTheBotViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(TrapBotGameState.initial(DifficultyLevel.EASY))
    val state: State<TrapBotGameState> = _state


    fun setDifficulty(difficulty: DifficultyLevel) {
        _state.value = TrapBotGameState.initial(difficulty)
    }



    fun onEvent(event: TrapTheBotEvent) {
        when (event) {
            is TrapTheBotEvent.BlockTile -> {
                val current = _state.value

                if (current.gameResult != null) return

                val updatedGrid = current.grid.map { row ->
                    row.map { tile ->
                        if (tile.row == event.row && tile.col == event.col && !tile.isBlocked && !tile.isBot) {
                            tile.copy(isBlocked = true)
                        } else tile
                    }
                }



                val botNextPosition = getBotNextMove(
                    grid = updatedGrid,
                    botPos = current.botPosition,
                    difficulty = current.difficulty
                )

                val gridAfterBotMove = moveBot(
                    grid = updatedGrid,
                    oldPosition = current.botPosition,
                    newPosition = botNextPosition
                )

           //     val newBotPos = moveBot(updatedGrid, current.botPosition)

                val result = calculateGameResult(gridAfterBotMove, botNextPosition)

                _state.value = current.copy(
                    grid = gridAfterBotMove,
                    botPosition = botNextPosition,
                    playerMoves = current.playerMoves + 1,
                    gameResult = result
                )
            }

            is TrapTheBotEvent.Restart -> {
                _state.value = TrapBotGameState.initial(_state.value.difficulty)
            }

            is TrapTheBotEvent.SetDifficulty -> {
                _state.value = TrapBotGameState.initial(event.difficulty)
            }
        }
    }

    fun getBotNextMove(
        grid: List<List<Tile>>,
        botPos: Positioner,
        difficulty: DifficultyLevel
    ): Positioner {
        return when (difficulty) {
            DifficultyLevel.EASY -> getRandomValidMove(grid, botPos)
            DifficultyLevel.MEDIUM -> getShortestPathMove(grid, botPos)
            DifficultyLevel.HARD -> getSmartPredictiveMove(grid, botPos)
        }
    }

    fun getRandomValidMove(grid: List<List<Tile>>, botPos: Positioner): Positioner {
        val directions = listOf(
            -1 to 0, 1 to 0, 0 to -1, 0 to 1,
            -1 to -1, -1 to 1, 1 to -1, 1 to 1
        )
        val validMoves = directions.mapNotNull { (dx, dy) ->
            val newRow = botPos.row + dx
            val newCol = botPos.col + dy
            if (newRow in grid.indices && newCol in grid[0].indices) {
                val tile = grid[newRow][newCol]
                if (!tile.isBlocked && !tile.isBot) Positioner(newRow, newCol) else null
            } else null
        }
        return validMoves.randomOrNull() ?: botPos // stay if stuck
    }

    fun getShortestPathMove(grid: List<List<Tile>>, botPos: Positioner): Positioner {
        val queue = ArrayDeque<Pair<Positioner, List<Positioner>>>()
        val visited = mutableSetOf<Positioner>()
        queue.add(botPos to listOf())

        val directions = listOf(
            -1 to 0, 1 to 0, 0 to -1, 0 to 1,
            -1 to -1, -1 to 1, 1 to -1, 1 to 1
        )

        while (queue.isNotEmpty()) {
            val (current, path) = queue.removeFirst()
            if (isEdge(current, grid)) {
                return path.firstOrNull() ?: botPos
            }

            for ((dx, dy) in directions) {
                val newRow = current.row + dx
                val newCol = current.col + dy
                val next = Positioner(newRow, newCol)
                if (newRow in grid.indices && newCol in grid[0].indices &&
                    next !in visited &&
                    !grid[newRow][newCol].isBlocked
                ) {
                    visited.add(next)
                    queue.add(next to path + next)
                }
            }
        }

        return botPos // stuck
    }

    fun isEdge(pos: Positioner, grid: List<List<Tile>>): Boolean {
        return pos.row == 0 || pos.col == 0 || pos.row == grid.lastIndex || pos.col == grid[0].lastIndex
    }

    fun getSmartPredictiveMove(grid: List<List<Tile>>, botPos: Positioner): Positioner {
        val directions = listOf(
            -1 to 0, 1 to 0, 0 to -1, 0 to 1,
            -1 to -1, -1 to 1, 1 to -1, 1 to 1
        )

        val validMoves = directions.mapNotNull { (dx, dy) ->
            val newRow = botPos.row + dx
            val newCol = botPos.col + dy
            if (newRow in grid.indices && newCol in grid[0].indices) {
                val tile = grid[newRow][newCol]
                if (!tile.isBlocked && !tile.isBot) Positioner(newRow, newCol) else null
            } else null
        }

        return validMoves.minByOrNull { minOf(it.row, it.col, grid.size - 1 - it.row, grid[0].size - 1 - it.col) }
            ?: botPos
    }






    fun moveBot(
        grid: List<List<Tile>>,
        oldPosition: Positioner,
        newPosition: Positioner
    ): List<List<Tile>> {
        return grid.map { row ->
            row.map { tile ->
                when {
                    tile.row == oldPosition.row && tile.col == oldPosition.col -> tile.copy(isBot = false)
                    tile.row == newPosition.row && tile.col == newPosition.col -> tile.copy(isBot = true)
                    else -> tile
                }
            }
        }
    }


    private fun calculateGameResult(grid: List<List<Tile>>, botPos: Positioner): GameResult? {
        val directions = listOf(
            0 to 1, 0 to -1, 1 to 0, -1 to 0,
            1 to 1, 1 to -1, -1 to 1, -1 to -1
        )

        val canEscape = directions.any { (dr, dc) ->
            val newRow = botPos.row + dr
            val newCol = botPos.col + dc
            newRow in grid.indices && newCol in grid[0].indices &&
                    !grid[newRow][newCol].isBlocked
        }

        val isAtEdge = botPos.row == 0 || botPos.row == grid.lastIndex ||
                botPos.col == 0 || botPos.col == grid[0].lastIndex

        return when {
            !canEscape -> GameResult.Win
            isAtEdge -> GameResult.Lose
            else -> null
        }
    }



    private fun checkBotTrapped(grid: List<List<Tile>>, bot: Positioner): Boolean {
        val directions = listOf(
            -1 to 0, 1 to 0, 0 to -1, 0 to 1,
            -1 to -1, -1 to 1, 1 to -1, 1 to 1
        )
        return directions.all { (dr, dc) ->
            val r = bot.row + dr
            val c = bot.col + dc
            r !in grid.indices || c !in grid[r].indices || grid[r][c].isBlocked
        }
    }


}

