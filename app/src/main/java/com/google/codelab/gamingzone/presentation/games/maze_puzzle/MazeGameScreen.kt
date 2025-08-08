package com.google.codelab.gamingzone.presentation.games.maze_puzzle

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MazeGameScreen(viewModel: MazeViewModel = hiltViewModel<MazeViewModel>()) {
    val maze = viewModel.maze.value
    val playerRow = viewModel.playerRow.value
    val playerCol = viewModel.playerCol.value

    val animatedX by animateFloatAsState(targetValue = playerCol.toFloat(), label = "")
    val animatedY by animateFloatAsState(targetValue = playerRow.toFloat(), label = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .weight(1f)
            .padding(16.dp)) {
            MazeCanvas(maze = maze)

            Canvas(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
            ) {
                val cellSize = size.width / maze[0].size
                val cx = animatedX * cellSize + cellSize / 2
                val cy = animatedY * cellSize + cellSize / 2
                drawCircle(Color.Green, radius = cellSize / 4, center = Offset(cx, cy))
            }
        }

        // Movement buttons
        ControlButtons(
            onMove = { direction -> viewModel.move(direction) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlButtons(onMove: (Direction) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { onMove(Direction.UP) }) {
            androidx.compose.material3.Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = Color.White)
        }
        Row {
            IconButton(onClick = { onMove(Direction.LEFT) }) {
                androidx.compose.material3.Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Left", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(32.dp))
            IconButton(onClick = { onMove(Direction.RIGHT) }) {
                androidx.compose.material3.Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Right", tint = Color.White)
            }
        }
        IconButton(onClick = { onMove(Direction.DOWN) }) {
            androidx.compose.material3.Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = Color.White)
        }
    }
}

