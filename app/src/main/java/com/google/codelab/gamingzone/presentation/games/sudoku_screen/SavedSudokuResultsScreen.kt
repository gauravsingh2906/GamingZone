package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.nio.file.WatchEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.get

@Composable
fun SavedSudokuResultsScreen(
    viewModel: SavedSudokuResultsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val results by viewModel.results

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "📊 Sudoku Match History",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            if (results.isEmpty()) {
                Text("No past games found.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(results) { result ->
                        SudokuResultItem(result)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }




    }
}


@Composable
fun SudokuResultItem(result: SudokuResultEntity) {
    val board = result.userSolution.chunked(9).map { row ->
        row.map { it.toString().toInt() }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Difficulty: ${result.difficulty}", fontWeight = FontWeight.Bold)
                Text("⏱ ${formatTime(result.timeTakenSeconds)}")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("❌ Mistakes: ${result.mistakesMade}/3")
                Text("💡 Hints: ${result.hintsUsed}/3")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text("📅 ${formatDate(result.timestamp)}", style = MaterialTheme.typography.labelMedium)

            Spacer(modifier = Modifier.height(12.dp))

            SudokuMiniBoard(board)
        }
    }
}

@Composable
fun SudokuMiniBoard(board: List<List<Int>>) {
    Column(
        modifier = Modifier
            .aspectRatio(1f)
            .border(2.dp, Color.Black)
    ) {
        for (row in 0..8) {
            Row(Modifier.weight(1f)) {
                for (col in 0..8) {
                    val cell = board[row][col]
                    //    val isSelected = state.selectedCell == (row to col)
                    //   val isInvalid = (row to col) in state.invalidCells



//                    val animatedColor by animateColorAsState(
//                        targetValue = when {
//                            cell.isHint -> Color(0xFFB3FFB3) // light green
//                            cell.isSelected -> Color(0xFFCCE5FF)
//                            cell.isError -> Color(0xFFFFCCCC)
//                            isSelected -> Color(0xFFCCE5FF)
//                            isInvalid -> Color.Red.copy(alpha = 0.3f)
//                            else -> Color.White
//                        }, // animation duration
//                        label = ""
//                    )


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color.White)
                            .clickable {
                             //   onAction(SudokuAction.SelectCell(row, col))
                            }
                            .drawBehind {
                                val borderSize = 2.dp.toPx()
                                val thinBorderSize = 0.5.dp.toPx()

                                // Top border
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = if (row % 3 == 0) borderSize else thinBorderSize
                                )
                                // Bottom border
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = if ((row + 1) % 3 == 0) borderSize else thinBorderSize
                                )
                                // Start border
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = if (col % 3 == 0) borderSize else thinBorderSize
                                )
                                // End border
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(size.width, 0f),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = if ((col + 1) % 3 == 0) borderSize else thinBorderSize
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (cell != 0) {
                            Text(
                                text = cell.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}




fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}


