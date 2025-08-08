package com.google.codelab.gamingzone.presentation.games.maze_puzzle

fun generateMaze(rows: Int, cols: Int): List<List<MazeCell>> {
    val maze = List(rows) { List(cols) { MazeCell() } }
    val stack = mutableListOf<Pair<Int, Int>>()

    fun isInBounds(r: Int, c: Int) = r in 0 until rows && c in 0 until cols

    fun dfs(row: Int, col: Int) {
        maze[row][col].visited = true
        val directions = listOf(
            Direction.UP to Pair(row - 1, col),
            Direction.DOWN to Pair(row + 1, col),
            Direction.LEFT to Pair(row, col - 1),
            Direction.RIGHT to Pair(row, col + 1)
        ).shuffled()

        for ((dir, next) in directions) {
            val (nextRow, nextCol) = next
            if (isInBounds(nextRow, nextCol) && !maze[nextRow][nextCol].visited) {
                when (dir) {
                    Direction.UP -> {
                        maze[row][col].topWall = false
                        maze[nextRow][nextCol].bottomWall = false
                    }
                    Direction.DOWN -> {
                        maze[row][col].bottomWall = false
                        maze[nextRow][nextCol].topWall = false
                    }
                    Direction.LEFT -> {
                        maze[row][col].leftWall = false
                        maze[nextRow][nextCol].rightWall = false
                    }
                    Direction.RIGHT -> {
                        maze[row][col].rightWall = false
                        maze[nextRow][nextCol].leftWall = false
                    }
                }
                dfs(nextRow, nextCol)
            }
        }
    }

    dfs(0, 0)
    return maze
}
