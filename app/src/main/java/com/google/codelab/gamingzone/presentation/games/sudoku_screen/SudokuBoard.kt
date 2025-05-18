package com.google.codelab.gamingzone.presentation.games.sudoku_screen
//
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewLightDark
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun SudokuBoard(
//    board: List<List<Int>>,
//    onCellSelected: (Int, Int) -> Unit,
//    onNumberInput: (Int) -> Unit
//) {
//
//    Column {
//        board.forEachIndexed { rowIndex, row ->
//            Row {
//                row.forEachIndexed { colIndex, value ->
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .border(1.dp, Color.Black)
//                            .clickable {
//                                onCellSelected(rowIndex, colIndex)
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text = if (value == 0) "" else value.toString())
//                    }
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(12.dp))
//        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
//            (1..9).forEach { number ->
//                OutlinedButton(
//                    onClick = { onNumberInput(number) },
//                    modifier = Modifier.size(40.dp)
//                ) {
//                    Text(text = number.toString())
//                }
//
//            }
//        }
//    }
//}
//
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun Preview() {
//    SudokuBoard(
//        board = listOf(
//            listOf(5, 3, 1, 2, 7, 3, 4, 4, 6),
//            listOf(6, 6, 7, 1, 9, 5, 9, 8, 6),
//            listOf(1, 9, 8, 6, 7, 9, 5, 6, 9),
//            listOf(6, 9, 8, 6, 6, 7, 7, 6, 5),
//            listOf(6, 9, 8, 6, 6, 7, 7, 6, 5),
//            listOf(6, 9, 8, 6, 6, 7, 7, 6, 5),
//            listOf(6, 9, 8, 6, 6, 7, 7, 6, 5),
//            listOf(6, 9, 8, 6, 6, 7, 7, 6, 5),
//            listOf(6, 9, 8, 6, 6, 7, 7, 6, 5),
//        ),
//        onCellSelected = { _, _ -> },
//        onNumberInput = {}
//
//
//    )
//}