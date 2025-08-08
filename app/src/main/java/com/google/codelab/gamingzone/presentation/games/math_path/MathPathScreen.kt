package com.google.codelab.gamingzone.presentation.games.math_path
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun MathPathScreen(viewModel: MathGridViewModel = viewModel()) {
//    val state by viewModel.state.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Header
//        Text(
//            text = if (state.isGameOver) "Game Over" else "Target: ${state.targetSum}",
//            fontSize = 22.sp,
//            fontWeight = FontWeight.Bold,
//            color = if (state.isGameOver) Color.Red else Color.Black
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text("Score: ${state.score}  |  Moves Left: ${state.movesLeft}")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Grid
//        Column {
//            state.grid.forEach { row ->
//                Row {
//                    row.forEach { tile ->
//                        TileBox(
//                            tile = tile,
//                            isSelected = state.selectedTiles.contains(tile),
//                            onClick = { viewModel.onEvent(MathGridEvent.TileClicked(tile)) }
//                        )
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Buttons
//        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
//            Button(
//                onClick = { viewModel.onEvent(MathGridEvent.SubmitSelection) },
//                enabled = !state.isGameOver
//            ) {
//                Text("Submit")
//            }
//
//            Spacer(Modifier.width(12.dp))
//
//            Button(
//                onClick = { viewModel.onEvent(MathGridEvent.ShuffleGrid) },
//                enabled = !state.isGameOver
//            ) {
//                Text("Shuffle")
//            }
//
//            Spacer(Modifier.width(12.dp))
//
//            Button(onClick = { viewModel.onEvent(MathGridEvent.RestartGame) }) {
//                Text("Restart")
//            }
//        }
//    }
//}
//
//
//@Composable
//fun TileBox(
//    tile: MathTile,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    val bgColor = when {
//        isSelected -> Color(0xFF90CAF9)  // light blue
//        else -> Color(0xFFE0E0E0)        // grey
//    }
//
//    Box(
//        modifier = Modifier
//            .padding(4.dp)
//            .size(56.dp)
//            .background(bgColor, shape = RoundedCornerShape(8.dp))
//            .clickable(onClick = onClick),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = tile.value.toString(),
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//    }
//}
//
//
//
//
