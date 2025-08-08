package com.google.codelab.gamingzone.presentation.games.aqua_flow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AquaFlowGameScreen(
    viewModel: AquaFlowViewModel = viewModel()
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("💧 AquaFlow - Level 1")

        Spacer(modifier = Modifier.height(16.dp))

        AquaFlowGrid(
            grid = state.value.grid,
            onTileClick = { x, y -> viewModel.rotatePipeAt(x, y) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.simulateFlow() },
            enabled = !state.value.tankFilled
        ) {
            Text("Start Flow")
        }

        if (state.value.tankFilled) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "✅ Tank Filled!",
                color = Color(0xFF00C853),
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun AquaFlowGrid(
    grid: List<List<Tile>>,
    onTileClick: (x: Int, y: Int) -> Unit
) {
    Column {
        grid.forEach { row ->
            Row {
                row.forEach { tile ->
                    val color = getTileColor(tile)
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(2.dp)
                            .border(1.dp, Color.Gray)
                            .background(color)
                            .clickable {
                                if (tile.type == TileType.PIPE)
                                    onTileClick(tile.x, tile.y)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (tile.type == TileType.SOURCE) Text("S", fontWeight = FontWeight.Bold)
                        else if (tile.type == TileType.TANK) Text("T", fontWeight = FontWeight.Bold)
                        else if (tile.type == TileType.PIPE) {
                            Icon(
                                imageVector = pipeDirectionIcon(tile.pipeDirection),
                                contentDescription = "Pipe",
                                tint = if (tile.isFilled) Color.Blue else Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun pipeDirectionIcon(direction: PipeDirection): ImageVector {
    return when (direction) {
        PipeDirection.HORIZONTAL -> Icons.Default.Clear
        PipeDirection.VERTICAL -> Icons.Default.MoreVert
        PipeDirection.CORNER_TL -> Icons.Default.ArrowBack // You can create a custom icon later
        PipeDirection.CORNER_TR -> Icons.Default.ArrowForward
        PipeDirection.CORNER_BL -> Icons.Default.ArrowDropDown
        PipeDirection.CORNER_BR -> Icons.Default.KeyboardArrowUp
        else -> Icons.Default.Clear
    }
}



@Composable
//fun LevelSelectScreen(levels: List<LevelMeta>, onLevelClick: (Int) -> Unit) {
//    LazyColumn {
//        items(levels.size) { i ->
//            val level = levels[i]
//            Row(modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onLevelClick(i) }
//                .padding(16.dp)
//            ) {
//                Text("Level ${i + 1}: ${level.name}")
//                if (level.completed) Text("✅", Modifier.padding(start = 8.dp))
//            }
//        }
//    }
//}


fun getTileColor(tile: Tile): Color {
    return when {
        tile.type == TileType.SOURCE -> Color.Blue
        tile.type == TileType.TANK -> if (tile.isFilled) Color.Cyan else Color.Gray
        tile.type == TileType.PIPE -> if (tile.isFilled) Color.Blue else Color.LightGray
        else -> Color.White
    }
}

