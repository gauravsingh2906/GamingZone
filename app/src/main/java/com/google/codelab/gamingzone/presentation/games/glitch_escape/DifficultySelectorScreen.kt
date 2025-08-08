package com.google.codelab.gamingzone.presentation.games.glitch_escape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DifficultySelectorScreen(
    onSelected: (GlitchDifficulty) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Difficulty", fontSize = 24.sp, color = Color.White)
        Spacer(Modifier.height(16.dp))
        GlitchDifficulty.values().forEach { difficulty ->
            Button(onClick = { onSelected(difficulty) }) {
                Text(difficulty.name)
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
