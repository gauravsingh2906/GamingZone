package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//@Composable
//fun SudokuGameScreen(
//    id: String?=null,
//    state: SudokuGameState,
//    onAction: (SudokuGameAction) -> Unit,
//    onExit: () -> Unit,
//) {
////    LaunchedEffect(Unit) {
////        event.collectLatest {
////            when (it) {
////                is SudokuGameEvent.GameCompleted -> {
////                    // Navigate to result or show dialog
////                }
////            }
////        }
////    }
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//        Text(
//            text = "Sudoku - ${state.difficulty}",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        SudokuBoard(
//            board = state.board,
//            onCellSelected = { row, col -> onAction(SudokuGameAction.SelectCell(row, col)) },
//            onNumberInput = { number -> onAction(SudokuGameAction.EnterNumber(number)) }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//            Button(onClick = { onAction(SudokuGameAction.CheckBoard) }) {
//                Text("Check")
//            }
//            Button(onClick = { onAction(SudokuGameAction.ClearBoard) }) {
//                Text("Clear")
//            }
//            Button(onClick = onExit) {
//                Text("Exit")
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewSudokuGameScreen() {
//    SudokuGameScreen(
//        state = SudokuGameState(),
//        onAction = {},
//        onExit = {},
//    )
//}
//
//
//
