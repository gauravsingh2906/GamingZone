package com.google.codelab.gamingzone.presentation.games.trap_bot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TrapTheBotScreen(
    state: TrapBotGameState,
    onEvent: (TrapTheBotEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Trap the Bot - ${state.difficulty.name}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        state.gameResult?.let {
            Text(
                text = when (it) {
                    GameResult.Win -> "You Trapped the Bot!"
                    GameResult.Lose -> "Bot Escaped!"
                    else -> ""
                },
                color = if (it == GameResult.Win) Color.Green else Color.Red,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

//        LazyColumn {
//            items(state.grid.size) { rowIndex ->
//                Row(
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    state.grid[rowIndex].forEach { tile ->
//                        val color = when {
//                            tile.isBot -> Color.Red
//                            tile.isBlocked -> Color.DarkGray
//                            else -> Color.LightGray
//                        }
//
//                        Box(
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .size(36.dp)
//                                .background(color, shape = RoundedCornerShape(4.dp))
//                                .clickable {
//                                    if (!tile.isBlocked && !tile.isBot && state.gameResult == null) {
//                                        onEvent(TrapTheBotEvent.BlockTile(tile.row, tile.col))
//                                    }
//                                }
//                        )
//                    }
//                }
//            }
//        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Makes it a perfect square
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                state.grid.forEach { row ->
                    Row(modifier = Modifier.weight(1f)) {
                        row.forEach { tile ->
                            val color = when {
                                tile.isBot -> Color.Red
                                tile.isBlocked -> Color.DarkGray
                                else -> Color.LightGray
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(2.dp)
                                    .background(color, shape = RoundedCornerShape(4.dp))
                                    .clickable {
                                        if (!tile.isBlocked && !tile.isBot && state.gameResult == null) {
                                            onEvent(TrapTheBotEvent.BlockTile(tile.row, tile.col))
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onEvent(TrapTheBotEvent.Restart)
        }) {
            Text("Restart")
        }
    }
}





//@Composable
//fun GridUI(state: TrapTheBotState, onEvent: (TrapTheBotEvent) -> Unit) {
//    Column {
//        state.tiles.forEach { row ->
//            Row {
//                row.forEach { tile ->
//                    Box(
//                        modifier = Modifier
//                            .size(36.dp)
//                            .padding(2.dp)
//                            .background(
//                                color = when {
//                                    tile.isBot -> Color.Red
//                                    tile.isBlocked -> Color.Black
//                                    else -> Color.LightGray
//                                },
//                                shape = RoundedCornerShape(4.dp)
//                            )
//                            .clickable(enabled = !tile.isBlocked && !tile.isBot && !state.isGameOver) {
//                                onEvent(TrapTheBotEvent.BlockTile(tile.row, tile.col))
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//
//                    }
//                }
//            }
//        }
//    }
//}
