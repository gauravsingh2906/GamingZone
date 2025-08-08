package com.google.codelab.gamingzone.presentation.games.maze_puzzle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MazeGameScreen(viewModel: MazeViewModel = hiltViewModel()) {
    val maze = viewModel.maze.value
    val playerRow = viewModel.playerRow.value
    val playerCol = viewModel.playerCol.value

    val animatedX by animateFloatAsState(targetValue = playerCol.toFloat(), label = "")
    val animatedY by animateFloatAsState(targetValue = playerRow.toFloat(), label = "")

    if (viewModel.hasWon.value) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = { viewModel.restartGame() }) {
                    Text("Play Again")
                }
            },
            title = { Text("🎉 You Win!") },
            text = { Text("Great job reaching the goal!") }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010)), // dark background
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                val rows = maze.size
                val cols = maze[0].size
                val cellSize = size.width / cols

                for (row in 0 until rows) {
                    for (col in 0 until cols) {
                        val cell = maze[row][col]
                        val x = col * cellSize
                        val y = row * cellSize

                        if (cell.topWall) {
                            drawLine(Color.White, Offset(x, y), Offset(x + cellSize, y), strokeWidth = 4f)
                        }

                        if (cell.topWall) {
                            drawLine(Color.White, Offset(x, y), Offset(x + cellSize, y), strokeWidth = 4f)
                        }
                        if (cell.bottomWall) {
                            drawLine(Color.White, Offset(x, y + cellSize), Offset(x + cellSize, y + cellSize), strokeWidth = 4f)
                        }
                        if (cell.leftWall) {
                            drawLine(Color.White, Offset(x, y), Offset(x, y + cellSize), strokeWidth = 4f)
                        }
                        if (cell.rightWall) {
                            drawLine(Color.White, Offset(x + cellSize, y), Offset(x + cellSize, y + cellSize), strokeWidth = 4f)
                        }
                    }
                }

                // Draw the player
                val cx = animatedX * cellSize + cellSize / 2
                val cy = animatedY * cellSize + cellSize / 2
                drawCircle(Color.Green, radius = cellSize / 4, center = Offset(cx, cy))
            }
        }

        ControlButtons(
            onMove = { direction -> viewModel.move(direction) }
        )
    }
}

@Composable
fun ControlButtons(onMove: (Direction) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { onMove(Direction.UP) }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = Color.White)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onMove(Direction.LEFT) }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Left", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(32.dp))
            IconButton(onClick = { onMove(Direction.RIGHT) }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Right", tint = Color.White)
            }
        }
        IconButton(onClick = { onMove(Direction.DOWN) }) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = Color.White)
        }
    }
}
