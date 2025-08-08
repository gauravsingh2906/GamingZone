package com.google.codelab.gamingzone.presentation.games.fear_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt
@Composable
fun FearZoneScreen() {
    var state by remember { mutableStateOf(FearZoneState()) }
    val gridSize = 5

    fun movePlayer(dx: Int, dy: Int) {
        if (state.isGameOver || state.isCompleted) return

        val (px, py) = state.playerPos
        val newPx = (px + dx).coerceIn(0, gridSize - 1)
        val newPy = (py + dy).coerceIn(0, gridSize - 1)
        val newPlayer = newPx to newPy

        val newLocks = if (newPlayer in state.lockTiles)
            state.unlockedLocks + newPlayer else state.unlockedLocks

        val newSafeTile = if (newLocks.size == state.lockTiles.size) 0 to 4 else state.safeTile

        val newTurn = state.turnCount + 1
        val newEnemy = if (newTurn % 2 == 0) moveEnemyToward(newPlayer, state.enemyPos) else state.enemyPos

        val newFear = when {
            newPlayer == newEnemy -> 1f
            else -> (state.fearLevel + 0.1f).coerceAtMost(1f)
        }

        val gameOver = newPlayer == newEnemy || newFear >= 1f
        val completed = newPlayer == newSafeTile

        state = state.copy(
            playerPos = newPlayer,
            unlockedLocks = newLocks,
            safeTile = newSafeTile,
            turnCount = newTurn,
            enemyPos = newEnemy,
            fearLevel = newFear,
            isFlicker = newTurn % 6 < 3,
            isGameOver = gameOver,
            isCompleted = completed
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("FEAR ZONE", color = Color.White, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(progress = state.fearLevel)
        Spacer(Modifier.height(16.dp))

        // Grid
        for (y in 0 until gridSize) {
            Row {
                for (x in 0 until gridSize) {
                    val pos = x to y
                    val color = when {
                        state.playerPos == pos -> Color.White
                        state.enemyPos == pos -> Color.Red
                        pos in state.unlockedLocks -> Color.Green
                        pos in state.lockTiles -> Color.Yellow
                        pos == state.safeTile -> Color.Cyan
                        state.isFlicker -> Color.DarkGray
                        else -> Color.Gray
                    }
                    Box(
                        Modifier
                            .size(48.dp)
                            .border(1.dp, Color.Black)
                            .background(color)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            state.isGameOver -> {
                FearGameOverScreen { state = FearZoneState() }
            }
            state.isCompleted -> {
                FearVictoryScreen { state = FearZoneState() }
            }
            else -> {
                Row {
                    Button(onClick = { movePlayer(0, -1) }) { Text("↑") }
                }
                Row {
                    Button(onClick = { movePlayer(-1, 0) }) { Text("←") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { movePlayer(1, 0) }) { Text("→") }
                }
                Row {
                    Button(onClick = { movePlayer(0, 1) }) { Text("↓") }
                }
            }
        }
    }
}

@Composable
fun FearGameOverScreen(onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("☠ GAME OVER ☠", color = Color.Red, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestart, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
            Text("Retry", color = Color.White)
        }
    }
}

fun moveEnemyToward(player: Pair<Int, Int>, enemy: Pair<Int, Int>): Pair<Int, Int> {
    val (px, py) = player
    val (ex, ey) = enemy
    val dx = px - ex
    val dy = py - ey

    return when {
        abs(dx) > abs(dy) -> (ex + dx.sign) to ey // Move horizontally
        else -> ex to (ey + dy.sign)              // Move vertically
    }
}

@Composable
fun FearVictoryScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🧠 YOU ESCAPED THE FEAR", color = Color.Cyan, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNext, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
            Text("Continue", color = Color.White)
        }
    }
}




//@Composable
//fun FearZoneScreen(viewModel: FearZoneViewModel = hiltViewModel()) {
//    val tiles = viewModel.tiles
//    val player = viewModel.player
//    val fear = viewModel.fearLevel
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Fear Zone", color = Color.White, style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(Modifier.height(16.dp))
//
//        GridView(tiles)
//
//        Spacer(Modifier.height(16.dp))
//
//        FearMeter(fear)
//
//        Spacer(Modifier.height(16.dp))
//
//        ControlButtons { viewModel.movePlayer(it) }
//    }
//}


@Composable
fun GridView(tiles: List<Tile>) {
    val gridSize = sqrt(tiles.size.toDouble()).toInt()
    Column {
        for (row in 0 until gridSize) {
            Row {
                for (col in 0 until gridSize) {
                    val tile = tiles[row * gridSize + col]
                    TileBox(tile)
                }
            }
        }
    }
}

@Composable
fun TileBox(tile: Tile) {
    val color = when {
        !tile.isVisible -> Color.DarkGray
        tile.isPlayerOn -> Color.White
        tile.type == TileType.SPIKE -> Color.Red
        tile.type == TileType.SAFE -> Color.Cyan
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .padding(2.dp)
            .background(color, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (tile.isPlayerOn) Text("👁", color = Color.Black)
    }
}


@Composable
fun ControlButtons(onMove: (Direction) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { onMove(Direction.UP) }) { androidx.compose.material3.Icon(Icons.Default.KeyboardArrowUp, null, tint = Color.White) }
        Row {
            IconButton(onClick = { onMove(Direction.LEFT) }) { androidx.compose.material3.Icon(Icons.Default.KeyboardArrowLeft, null, tint = Color.White) }
            Spacer(Modifier.width(20.dp))
            IconButton(onClick = { onMove(Direction.RIGHT) }) { androidx.compose.material3.Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White) }
        }
        IconButton(onClick = { onMove(Direction.DOWN) }) { androidx.compose.material3.Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White) }
    }
}



