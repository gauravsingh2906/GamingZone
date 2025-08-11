package com.google.codelab.gamingzone.presentation.games.math_path

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.codelab.gamingzone.presentation.game_detail_screen.GameMode
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.profile_screen.UserStatsViewModel
import kotlinx.coroutines.delay

@Composable
fun MathPathGameScreen(
    mode: GameMode,
    difficulty: DifficultyMath?,
    state: MathPathState,
    onAction: (MathGridEvent) -> Unit,
    navigateToGameOverScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (mode) {
        GameMode.LEVEL -> MathPathLevelUI(
            state = state,
            onAction = onAction,
            navigateToGameOverScreen = navigateToGameOverScreen
        )

        GameMode.TIMED -> MathPathTimedUI(
            state = state,
            difficulty = difficulty!!,
            onAction = onAction
        )
    }
    if (state.confettiTrigger) {
        ConfettiAnimation()
    }
}


@Composable
fun ConfettiAnimation() {
    Text(
        text = "🎊🎉✨🎉🎊",
        fontSize = 36.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MathPathLevelUI(
    state: MathPathState,
    onAction: (MathGridEvent) -> Unit,
    navigateToGameOverScreen: () -> Unit,
    profileViewModel: UserStatsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("No difficulty")
        Text("Level: ${state.level}", style = MaterialTheme.typography.titleLarge)
        Text("Score: ${state.score} | Streak: ${state.streak}")
        Text("Target: ${state.targetSum} | Current: ${state.selectedTiles.sumOf { it.value }}")

        Spacer(Modifier.height(16.dp))
        MathTileGrid(
            grid = state.grid,
            selectedTiles = state.selectedTiles,
            onTileClick = {
                onAction(MathGridEvent.SelectTile(it))
            }
        )

        Spacer(Modifier.height(16.dp))
        Button(onClick = { onAction(MathGridEvent.SubmitSelection) }) {
            Text("Submit")
        }


        val showLoseDialog = remember { mutableStateOf(false) }
        if (state.isGameOver) {
            profileViewModel.recordGameResult(
                gameName = "mathpath",
                isWin = false,
                isDraw = false,
                xpEarned = if (state.level<10) 10 else 30
            )
            Spacer(Modifier.height(16.dp))
            AlertDialog(
                onDismissRequest = { },
                title = { Text("😢 You Lost!") },
                text = {
                    Text(
                        modifier = Modifier
                            .height(25.dp),
                        text = "Your current sum is greater then target:${state.targetSum}",
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showLoseDialog.value = false
                        navigateToGameOverScreen()
                    }) {
                        Text("OK")
                    }
                }
            )
            Text("Level Complete! 🎉")
            Button(onClick = { onAction(MathGridEvent.NextLevel) }) {
                profileViewModel.recordGameResult(
                    gameName = "mathpath",
                    isWin = true,
                    isDraw = false,
                    xpEarned = if (state.level<10) 10 else 30
                )
                Text("Next Level")
            }
        }
    }
}

@Composable
fun MathPathTimedUI(
    state: MathPathState,
    difficulty: DifficultyMath,
    onAction: (MathGridEvent) -> Unit
) {
    var timesLeft by remember {
        mutableStateOf(
            when (difficulty) {
                DifficultyMath.EASY -> 60
                DifficultyMath.MEDIUM -> 45
                DifficultyMath.HARD -> 30
            }
        )
    }

    LaunchedEffect(key1 = state.isGameOver) {
        if (!state.isGameOver) {
            while (timesLeft > 0) {
                delay(1000L)
                timesLeft--
            }
            if (timesLeft == 0) onAction(MathGridEvent.GameOver)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("⏱ Difficulty: ${difficulty.name}", style = MaterialTheme.typography.titleMedium)
        Text(
            "Time Left: ${timesLeft} sec",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Red
        )
        Text("Score: ${state.score}", style = MaterialTheme.typography.titleMedium)
        Text("Streak: ${state.streak}", style = MaterialTheme.typography.titleMedium)
        Text("Target: ${state.targetSum} | Current: ${state.selectedTiles.sumOf { it.value }}")

        Spacer(Modifier.height(16.dp))
        MathTileGrid(state.grid, state.selectedTiles, onTileClick = {
            onAction(MathGridEvent.SelectTile(it))
        })

        Spacer(Modifier.height(16.dp))
        Button(onClick = { onAction(MathGridEvent.SubmitSelection) }) {
            Text("Submit")
        }

        if (state.isGameOver) {
            Spacer(Modifier.height(16.dp))
            Text("Game Over ⏰", color = Color.Red, style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { onAction(MathGridEvent.RestartGame) }) {
                Text("Try Again")
            }
        }
    }
}

@Composable
fun MathTileGrid(
    grid: List<List<MathTile>>,
    selectedTiles: List<MathTile>,
    onTileClick: (MathTile) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grid.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { tile ->
                    val isSelected = selectedTiles.any { it.row == tile.row && it.col == tile.col }
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFFBBDEFB) else Color.LightGray)
                            .clickable { onTileClick(tile) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tile.value.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isSelected) Color.Black else Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}



