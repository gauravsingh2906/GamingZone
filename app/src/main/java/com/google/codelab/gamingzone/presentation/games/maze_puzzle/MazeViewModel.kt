package com.google.codelab.gamingzone.presentation.games.maze_puzzle

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@HiltViewModel
class MazeViewModel @Inject constructor() : ViewModel() {

    private val _maze = mutableStateOf(generateMaze(8, 8))
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    val maze: State<List<List<MazeCell>>> = _maze

    private val _playerRow = mutableStateOf(0)
    private val _playerCol = mutableStateOf(0)

    val playerRow: State<Int> = _playerRow
    val playerCol: State<Int> = _playerCol

    fun move(direction: Direction) {
        val mazeGrid = _maze.value
        val current = mazeGrid[_playerRow.value][_playerCol.value]

        when (direction) {
            Direction.UP -> if (!current.top) _playerRow.value -= 1
            Direction.DOWN -> if (!current.bottom) _playerRow.value += 1
            Direction.LEFT -> if (!current.left) _playerCol.value -= 1
            Direction.RIGHT -> if (!current.right) _playerCol.value += 1
        }
    }
}

enum class Direction { UP, DOWN, LEFT, RIGHT }
