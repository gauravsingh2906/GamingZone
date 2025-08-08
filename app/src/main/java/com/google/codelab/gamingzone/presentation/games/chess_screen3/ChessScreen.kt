package com.google.codelab.gamingzone.presentation.games.chess_screen3

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChessScreen(
    viewModel: ChessViewModel= hiltViewModel<ChessViewModel>(),
) {
    val state = viewModel.state.collectAsState().value

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (state.gameOver) {
                "${state.winner} wins!"
            } else {
                "${state.currentTurn}'s Turn" + if (state.isInCheck) " (Check)" else ""
            },
            modifier = Modifier.padding(16.dp)
        )

        for (row in 0..7) {
            Row {
                for (col in 0..7) {
                    val piece = state.board[row][col]
                    val backgroundColor = if ((row + col) % 2 == 0) Color.White else Color.Gray
                    val isHighlighted = state.validMoves.contains(Position(row, col))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(if (isHighlighted) Color.Yellow else backgroundColor)
                            .border(1.dp, Color.Black)
                            .clickable { viewModel.onSquareClicked(row,col) },
                        contentAlignment = Alignment.Center
                    ) {
                        piece?.let {
                            Text(
                                text = piece.symbol,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun ChessScreen(
//    state: ChessState,
//    onAction: (ChessAction) -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFEFFFEF))
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.align(Alignment.TopCenter)
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "${state.currentTurn.name}'s Turn",
//                style = MaterialTheme.typography.titleMedium
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            ChessBoard(state, onAction)
//        }
//
//        if (state.gameOver) {
//            AlertDialog(
//                onDismissRequest = {
//                    onAction(ChessAction.OnGameDialogDismissed)
//                },
//                title = { Text("Game Over") },
//                text = {
//                    Text(
//                        when (state.winner) {
//                            PlayerColor.WHITE -> "White wins!"
//                            PlayerColor.BLACK -> "Black wins!"
//                            null -> "It's a draw!"
//                        }
//                    )
//                },
//                confirmButton = {
//                    TextButton(onClick = {
//                        onAction(ChessAction.OnGameDialogDismissed)
//                    }) {
//                        Text("OK")
//                    }
//                }
//            )
//        }
//    }
//}
//
//
//@Composable
//fun ChessBoard(
//    state: ChessState,
//    onAction: (ChessAction) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .aspectRatio(1f)
//    ) {
//        for (row in 0..7) {
//            Row(Modifier.weight(1f)) {
//                for (col in 0..7) {
//                    val piece = state.board[row][col]
//                    val isDark = (row + col) % 2 != 0
//                    val isHighlighted = state.validMoves.contains(Position(row, col))
//
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .background(
//                                when {
//                                    isHighlighted -> Color.Yellow.copy(alpha = 0.5f)
//                                    isDark -> Color.Gray
//                                    else -> Color.White
//                                }
//                            )
//                            .clickable {
//                                onAction(ChessAction.OnSquareSelected(Position(row, col)))
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        piece?.let {
//                            Text(
//                                text = piece.symbol,
//                                fontSize = 24.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}






