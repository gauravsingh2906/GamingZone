package com.google.codelab.gamingzone.presentation.games.math_trap_dungeon

import androidx.lifecycle.ViewModel
import com.google.codelab.gamingzone.domain.math_dungeon.Direction
import com.google.codelab.gamingzone.domain.math_dungeon.MathDungeonRepository
import com.google.codelab.gamingzone.domain.math_dungeon.MathQuestion
import com.google.codelab.gamingzone.domain.math_dungeon.PlayerState
import com.google.codelab.gamingzone.domain.math_dungeon.Tile
import com.google.codelab.gamingzone.domain.math_dungeon.TileType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.collections.get
import kotlin.text.get

@HiltViewModel
class MathDungeonViewModel @Inject constructor(
    private val repo: MathDungeonRepository
) : ViewModel() {

    private val _dungeon = MutableStateFlow<List<List<Tile>>>(emptyList())
    val dungeon = _dungeon.asStateFlow()

    private var currentLevel = 1
    private var currentSize = 5


    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    private val _currentQuestion = MutableStateFlow<MathQuestion?>(null)
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _activeTrapTile = MutableStateFlow<Tile?>(null)
    val activeTrapTile = _activeTrapTile.asStateFlow()


    init {
        generateDungeon()
    }

    fun generateDungeon(rows: Int = 5, cols: Int = 5) {
        _dungeon.value = repo.generateDungeon(rows, cols)
        _playerState.value = PlayerState()
    }

    fun movePlayerTo(tile: Tile) {
        val current = _playerState.value
        val (currentRow, currentCol) = current.position
        val lives = current.lives
        if (lives <= 0) return

        val isAdjacent = (tile.row == currentRow && (tile.col == currentCol - 1 || tile.col == currentCol + 1)) ||
                (tile.col == currentCol && (tile.row == currentRow - 1 || tile.row == currentRow + 1))

        if (!isAdjacent) return // ❌ Prevent far jumps

        // 🔴 Handle trap tile
        if (tile.type == TileType.Trap && !tile.visited && tile.question != null) {
            _currentQuestion.value = tile.question
            _activeTrapTile.value = tile
            return // 🛑 Wait for user to answer
        }

        // ✅ Move to tile directly for other types or visited traps
        _playerState.value = current.copy(position = tile.row to tile.col)

        if (tile.type == TileType.Exit) {
            onLevelComplete()
        }

        markTileVisited(tile)
    }






    fun consumePotion(tile: Tile) {
        // Give extra life or score
        _playerState.update { it.copy(lives = it.lives + 1) }
        markTileVisited(tile)
    }

    fun reachExit(tile: Tile) {
        onLevelComplete()
    }

    private fun onLevelComplete() {
        currentLevel++
        currentSize = (5 + (currentLevel / 2)).coerceAtMost(9)
        generateDungeon(currentSize, currentSize)
    }

    private fun markTileVisited(tile: Tile) {
        _dungeon.update {
            it.map { row -> row.map { t ->
                if (t.row == tile.row && t.col == tile.col) t.copy(visited = true) else t
            }}
        }
    }


    fun clearCurrentTrap() {
        _currentQuestion.value = null
        _activeTrapTile.value = null
    }



    fun submitAnswer(tile: Tile, input: Int) {
        val isCorrect = tile.question?.answer == input
        val player = _playerState.value

        if (isCorrect) {
            _playerState.value = player.copy(
                score = player.score + 10,
                position = tile.row to tile.col
            )
            markTileVisited(tile)
        } else {
            _playerState.value = player.copy(lives = player.lives - 1)
        }

        _currentQuestion.value = null
        _activeTrapTile.value = null
    }




    fun onTileClick(x: Int, y: Int) {
        dungeon.value.getOrNull(x)?.getOrNull(y)?.let { tile ->
            movePlayerTo(tile)
        }
    }



    fun movePlayer(direction: Direction) {
        // ⛔ Prevent movement while a question is active
        if (_currentQuestion.value != null) return

        val player = _playerState.value
        val (row, col) = player.position
        val newRow = when (direction) {
            Direction.UP -> row - 1
            Direction.DOWN -> row + 1
            else -> row
        }
        val newCol = when (direction) {
            Direction.LEFT -> col - 1
            Direction.RIGHT -> col + 1
            else -> col
        }

        if (newRow in dungeon.value.indices && newCol in dungeon.value[0].indices) {
            val targetTile = dungeon.value[newRow][newCol]
            movePlayerTo(targetTile)
        }
    }



}
