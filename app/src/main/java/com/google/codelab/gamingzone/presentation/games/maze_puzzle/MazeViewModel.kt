package com.google.codelab.gamingzone.presentation.games.maze_puzzle

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import java.util.ArrayDeque

class MazeViewModel : ViewModel() {

    private val rows = 6
    private val cols = 6

    private val _maze = mutableStateOf(generateMaze(rows, cols))
    val maze: State<List<List<MazeCell>>> = _maze

    private val _playerRow = mutableStateOf(0)
    private val _playerCol = mutableStateOf(0)
    val playerRow: State<Int> = _playerRow
    val playerCol: State<Int> = _playerCol

    // ✅ Win state
    private val _hasWon = mutableStateOf(false)
    val hasWon: State<Boolean> = _hasWon

    fun move(direction: Direction) {
        val row = _playerRow.value
        val col = _playerCol.value
        val current = _maze.value[row][col]

        val (newRow, newCol) = when (direction) {
            Direction.UP -> if (!current.topWall) row - 1 to col else row to col
            Direction.DOWN -> if (!current.bottomWall) row + 1 to col else row to col
            Direction.LEFT -> if (!current.leftWall) row to col - 1 else row to col
            Direction.RIGHT -> if (!current.rightWall) row to col + 1 else row to col
        }

        if (newRow != row || newCol != col) {
            _playerRow.value = newRow
            _playerCol.value = newCol

            // ✅ Check for win
            if (newRow == rows - 1 && newCol == cols - 1) {
                _hasWon.value = true
            }
        }
    }

    fun restartGame() {
        _maze.value = generateMaze(rows, cols)
        _playerRow.value = 0
        _playerCol.value = 0
        _hasWon.value = false
    }
}




enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0)
}

