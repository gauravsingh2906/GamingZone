package com.google.codelab.gamingzone.presentation.games.math_trap_dungeon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.codelab.gamingzone.domain.math_dungeon.Direction
import com.google.codelab.gamingzone.domain.math_dungeon.MathQuestion
import com.google.codelab.gamingzone.domain.math_dungeon.PlayerState
import com.google.codelab.gamingzone.domain.math_dungeon.Tile
import com.google.codelab.gamingzone.domain.math_dungeon.TileType
import kotlinx.coroutines.coroutineScope

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DungeonLegend() {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LegendItem(Color.Red, "Trap")
        LegendItem(Color.Green, "Potion")
        LegendItem(Color.Yellow, "Exit")
        LegendItem(Color.LightGray, "Path")
        LegendItem(Color.DarkGray, "Unvisited")
        LegendItem(Color.Blue, "You")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
                .border(1.dp, Color.Black)
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp)
    }
}


@Composable
fun MathTrapDungeonScreen(viewModel: MathDungeonViewModel = hiltViewModel()) {
    val dungeon by viewModel.dungeon.collectAsState()
    val player by viewModel.playerState.collectAsState()
    val trapTile by viewModel.activeTrapTile.collectAsState()
    val question by viewModel.currentQuestion.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Legend at top
        DungeonLegend()

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Game stats
        Text("Lives: ${player.lives} | Score: ${player.score}")

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Grid of dungeon tiles
        DungeonGrid(
            dungeon = dungeon,
            player = player,
            onTileClick = { tile ->
                viewModel.movePlayerTo(tile)
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        MovementControls { direction -> viewModel.movePlayer(direction) }


        // 🔹 Math question dialog when trap tile is triggered
        if (question != null && trapTile != null) {
            MathTrapDialog(
                question = question!!,
                onSubmit = { answer ->
                    viewModel.submitAnswer(tile = trapTile!!, input = answer)
                },
                onDismiss = {
                    viewModel.clearCurrentTrap()
                }
            )
        }
    }
}

@Composable
fun DungeonGrid(
    dungeon: List<List<Tile>>,
    player: PlayerState,
    onTileClick: (Tile) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 32.dp)
    ) {
        dungeon.forEach { row ->
            Row {
                row.forEach { tile ->
                    val isPlayerHere = tile.row == player.position.first && tile.col == player.position.second
                    val visible = tile.visited || isPlayerHere

                    DungeonTile(
                        tile = tile,
                        isPlayerHere = isPlayerHere,
                        isVisible = visible,
                        onClick = {
                            if (visible) onTileClick(tile)
                        }
                    )

                }
            }
        }
    }
}


@Composable
fun DungeonTile(
    tile: Tile,
    isPlayerHere: Boolean,
    isVisible: Boolean = true,
    onClick: () -> Unit
) {
    val tileColor = when (tile.type) {
        TileType.Trap -> Color.Red
        TileType.Empty -> Color.LightGray
        TileType.Potion -> Color.Green
        TileType.Exit -> Color.Yellow
    }

    val backgroundColor = when {
        isPlayerHere -> Color.Blue
        !isVisible -> Color.DarkGray
        else -> tileColor
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .border(1.dp, Color.Black)
            .background(backgroundColor)
            .then(if (isVisible) Modifier.clickable { onClick() } else Modifier)
    ) {
        if (isPlayerHere) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Player",
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (tile.visited && isVisible) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.Magenta)
                    .align(Alignment.Center)
            )
        }
    }
}







@Composable
fun MovementControls(onMove: (Direction) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { onMove(Direction.UP) }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
        }
        Row {
            IconButton(onClick = { onMove(Direction.LEFT) }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(48.dp))
            IconButton(onClick = { onMove(Direction.RIGHT) }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
        IconButton(onClick = { onMove(Direction.DOWN) }) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
    }
}

@Composable
fun MathTrapDialog(
    question: MathQuestion,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var answer by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Solve to continue!") },
        text = {
            Column {
                Text(text = question.question)
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = answer,
                    onValueChange = { answer = it.filter(Char::isDigit) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (answered && resultMessage != null) {
                    Text(
                        text = resultMessage!!,
                        color = if (resultMessage!!.contains("Correct")) Color.Green else Color.Red
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val input = answer.toIntOrNull()
                answered = true
                resultMessage = if (input == question.answer) {
                    "Correct!"
                } else {
                    "Wrong! The correct answer is ${question.answer}"
                }
                answered = true
                // Delay hiding until shown

                coroutineScope.launch {
                    kotlinx.coroutines.delay(1500)
                    onSubmit(input ?: -1)
                    onDismiss()
                }

            },
                enabled = !answered) {
                Text("Submit")
            }
        },
        dismissButton = {

        }
    )
}





