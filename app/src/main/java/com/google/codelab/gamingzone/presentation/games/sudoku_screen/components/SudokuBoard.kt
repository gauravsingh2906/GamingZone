package com.google.codelab.gamingzone.presentation.games.sudoku_screen.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.google.codelab.gamingzone.presentation.sudoku_screen.SudokuCell
//
//@Composable
//fun SudokuBoarder(
//    board: List<List<SudokuCell>>,
//    selectedCell: Pair<Int, Int>?,
//    invalidCells: Set<Pair<Int, Int>>,
//    onCellClick:(Int, Int)-> Unit
//) {
//
//    Column {
//        for (row in 0 until 9) {
//            Row {
//                for (col in 0 until 9) {
//                    val cell = board[row][col]
//                    val isSelected = selectedCell == row to col
//                    val isInvalid = invalidCells.contains(row to col)
//
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .border(
//                                width = 2.dp,
//                                color = when {
//                                    isInvalid -> Color.Red
//                                    isSelected -> Color.Cyan
//                                    else -> Color.Black
//                                }
//                            )
//                            .border(
//                                width = if ((col + 1) % 3 == 0 && col != 8) 1.dp else 2.dp,
//                                color = Color.Black
//                            )
//                            .border(
//                                width = if ((row + 1) % 3 == 0 && row != 8) 1.dp else 2.dp,
//                                color = Color.Black
//                            )
//                            .background(
//                                if (cell.isFixed) Color.LightGray else Color.White
//                            )
//                            .clickable { onCellClick(row, col) },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        if (cell.value != 0) {
//                            Text(
//                                text = cell.value.toString(),
//                                fontWeight = if (cell.isFixed) FontWeight.Normal else FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun NumberSelector(
//    onDigitSelected: (Int) -> Unit
//) {
//    Column {
//        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
//            (1..5).forEach { digit ->
//                OutlinedButton(onClick = { onDigitSelected(digit) }, modifier = Modifier.weight(1f)) {
//                    Text(digit.toString())
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(4.dp))
//        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
//            (6..9).forEach { digit ->
//                OutlinedButton(onClick = { onDigitSelected(digit) }, modifier = Modifier.weight(1f)) {
//                    Text(digit.toString())
//                }
//            }
//        }
//    }
//}
