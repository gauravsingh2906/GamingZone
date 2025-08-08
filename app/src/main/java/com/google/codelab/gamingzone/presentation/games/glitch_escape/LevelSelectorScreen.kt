package com.google.codelab.gamingzone.presentation.games.glitch_escape

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LevelSelectorScreen(
    maxUnlockedLevel: Int,
    difficulty: GlitchDifficulty,
    onLevelSelected: (LevelData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Level", fontSize = 24.sp, color = Color.White)
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(20) { index ->
                val isUnlocked = index <= maxUnlockedLevel
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                        .background(if (isUnlocked) Color.Gray else Color.DarkGray)
                        .clickable(enabled = isUnlocked) {
                            val level = generateLevel(index, difficulty)
                            onLevelSelected(level)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("${index + 1}", color = if (isUnlocked) Color.White else Color.LightGray)
                }
            }
        }
    }
}
