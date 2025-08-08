package com.google.codelab.gamingzone.presentation.games.maze_puzzle

import android.os.Build
import androidx.annotation.RequiresApi

data class MazeCell(
    val row: Int,
    val col: Int,
    var visited: Boolean = false,
    var top: Boolean = true,
    var right: Boolean = true,
    var bottom: Boolean = true,
    var left: Boolean = true
)

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun generateMaze(rows: Int, cols: Int): List<List<MazeCell>> {
    val maze = List(rows) { row ->
        List(cols) { col -> MazeCell(row, col) }
    }

    val visited = Array(rows) { BooleanArray(cols) }
    val stack = ArrayDeque<MazeCell>() // 👈 replace List with ArrayDeque

    val start = maze[0][0]
    stack.add(start)

    while (stack.isNotEmpty()) {
        val current = stack.removeLast() // ✅ safe now

        val neighbors = getUnvisitedNeighbors(current, maze)
        if (neighbors.isNotEmpty()) {
            stack.addLast(current)
            val next = neighbors.random()
            removeWall(current, next)
            visited[next.row][next.col] = true
            stack.addLast(next)
        }
    }

    return maze
}


fun getUnvisitedNeighbors(cell: MazeCell, grid: List<List<MazeCell>>): List<MazeCell> {
    val neighbors = mutableListOf<MazeCell>()
    val (row, col) = cell.row to cell.col

    if (row > 0 && !grid[row - 1][col].visited) neighbors.add(grid[row - 1][col])
    if (row < grid.size - 1 && !grid[row + 1][col].visited) neighbors.add(grid[row + 1][col])
    if (col > 0 && !grid[row][col - 1].visited) neighbors.add(grid[row][col - 1])
    if (col < grid[0].size - 1 && !grid[row][col + 1].visited) neighbors.add(grid[row][col + 1])

    return neighbors
}

fun removeWall(current: MazeCell, next: MazeCell) {
    val dx = next.col - current.col
    val dy = next.row - current.row

    when {
        dx == 1 -> {
            current.right = false
            next.left = false
        }
        dx == -1 -> {
            current.left = false
            next.right = false
        }
        dy == 1 -> {
            current.bottom = false
            next.top = false
        }
        dy == -1 -> {
            current.top = false
            next.bottom = false
        }
    }
}


