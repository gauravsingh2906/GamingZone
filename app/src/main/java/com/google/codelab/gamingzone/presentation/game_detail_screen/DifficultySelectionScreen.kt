package com.google.codelab.gamingzone.presentation.game_detail_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.codelab.gamingzone.presentation.games.trap_bot.DifficultyLevel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySelectionScreen(
    onDifficultySelected: (DifficultyLevel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Difficulty") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose how smart the Bot will be:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            DifficultyLevel.values().forEach { difficulty ->
                Button(
                    onClick = {
                        onDifficultySelected(difficulty)
                       // navController.navigate("trap_bot_game?difficulty=${difficulty.name}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (difficulty) {
                            DifficultyLevel.EASY -> Color(0xFF81C784)
                            DifficultyLevel.MEDIUM -> Color(0xFFFFF176)
                            DifficultyLevel.HARD -> Color(0xFFE57373)
                        }
                    )
                ) {
                    Text(
                        text = difficulty.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


