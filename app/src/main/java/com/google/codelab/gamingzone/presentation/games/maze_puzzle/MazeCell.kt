package com.google.codelab.gamingzone.presentation.games.maze_puzzle

data class MazeCell(
    var topWall: Boolean = true,
    var bottomWall: Boolean = true,
    var leftWall: Boolean = true,
    var rightWall: Boolean = true,
    var visited: Boolean = false
)
