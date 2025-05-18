package com.google.codelab.gamingzone.presentation.games.color_race_screen2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColorRaceScreen(
    state: ColorRaceState,
    onEvent: (ColorRaceEvent) -> Unit
) {
    val blinkColor = if (
        state.isShowingSequence &&
        state.currentBlinkIndex in state.sequence.indices
    ) {
        state.sequence[state.currentBlinkIndex].color
    } else Color.Gray

    // Game Over Dialog
    if (state.showGameOverDialog) {
        AlertDialog(
            onDismissRequest = {
                onEvent(ColorRaceEvent.DismissGameOverDialog)
            },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(ColorRaceEvent.DismissGameOverDialog)
                }) {
                    Text("OK")
                }
            },
            title = { Text("Game Over") },
            text = { Text("You reached Level ${state.level}. Try again!") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (state.isShowingSequence) "Watch the Sequence" else "Repeat the Sequence",
            fontSize = 22.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.size(200.dp)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ColorButton(Color.Red, blinkColor == Color.Red) {
                        if (!state.isShowingSequence) onEvent(ColorRaceEvent.ColorClicked(ColorOption.RED))
                    }
                    ColorButton(Color.Green, blinkColor == Color.Green) {
                        if (!state.isShowingSequence) onEvent(ColorRaceEvent.ColorClicked(ColorOption.GREEN))
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ColorButton(Color.Blue, blinkColor == Color.Blue) {
                        if (!state.isShowingSequence) onEvent(ColorRaceEvent.ColorClicked(ColorOption.BLUE))
                    }
                    ColorButton(Color.Yellow, blinkColor == Color.Yellow) {
                        if (!state.isShowingSequence) onEvent(ColorRaceEvent.ColorClicked(ColorOption.YELLOW))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        if (!state.isShowingSequence && state.sequence.isEmpty()) {
            Button(onClick = { onEvent(ColorRaceEvent.StartGame) }) {
                Text("Start Game")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Level: ${state.level}",
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ColorButton(color: Color, isBlinking: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(if (isBlinking) color else color.copy(alpha = 0.4f))
            .clickable { onClick() }
    ) {

    }
}