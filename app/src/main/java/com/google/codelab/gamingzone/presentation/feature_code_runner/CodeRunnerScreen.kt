package com.google.codelab.gamingzone.presentation.feature_code_runner



import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.google.codelab.gamingzone.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel

import kotlinx.coroutines.delay

@Composable
fun CodeRunnerScreen(viewModel: CodeRunnerViewModel = viewModel()) {
    val playerState by viewModel.playerState.collectAsState()

    // Background layers
    val bgFar = painterResource(id = R.drawable.bg_layer_far)
    val bgMid = painterResource(id = R.drawable.bg_layer_mid)
    val bgNear = painterResource(id = R.drawable.bg_layer_near)

    val runFrames = listOf(R.drawable.run_1, R.drawable.run_2, R.drawable.run_3, R.drawable.run_4)
    val idleFrames = listOf(R.drawable.idle_1, R.drawable.idle_2, R.drawable.idle_3)
    val jumpFrames = listOf(R.drawable.jump_1, R.drawable.jump_2)
    val dashFrames = listOf(R.drawable.dash_1, R.drawable.dash_2, R.drawable.dash_3)

    val currentFrames = when (playerState.action) {
        PlayerAction.RUN -> runFrames
        PlayerAction.JUMP -> jumpFrames
        PlayerAction.DASH -> dashFrames
        PlayerAction.TELEPORT -> dashFrames
        else -> idleFrames
    }

    var frameIndex by remember { mutableIntStateOf(0) }

    // Animate player sprite frames
    LaunchedEffect(playerState.action) {
        frameIndex = 0
        while (true) {
            delay(100)
            frameIndex = (frameIndex + 1) % currentFrames.size
        }
    }

    // Camera offset to follow player
    val cameraOffsetX = remember { Animatable(0f) }
    LaunchedEffect(playerState.x) {
        cameraOffsetX.animateTo(playerState.x - 150f) // Adjust offset target
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Game world area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds()
        ) {
            // Parallax background layers
            Image(
                painter = bgFar,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-cameraOffsetX.value * 0.2f).dp)
            )
            Image(
                painter = bgMid,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-cameraOffsetX.value * 0.5f).dp)
            )
            Image(
                painter = bgNear,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-cameraOffsetX.value * 1.0f).dp)
            )

            // Player sprite
            Image(
                painter = painterResource(id = currentFrames[frameIndex % currentFrames.size]),
                contentDescription = "Cyber Ninja",
                modifier = Modifier
                    .size(96.dp)
                    .offset(
                        x = (playerState.x - cameraOffsetX.value).dp,
                        y = (300 - playerState.y).dp
                    )
            )
        }

        // Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GameButton("Jump") { viewModel.jump() }
            GameButton("Dash") { viewModel.dash() }
            GameButton("Teleport") { viewModel.teleport() }
        }
    }
}

@Composable
fun GameButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(50.dp)
            .width(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00ADB5),
            contentColor = Color.White
        )
    ) {
        Text(text = text)
    }
}

