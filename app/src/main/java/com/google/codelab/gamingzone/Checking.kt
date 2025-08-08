package com.google.codelab.gamingzone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.codelab.gamingzone.presentation.ui.theme.upperLeaderboard

@Composable
fun InvertedSemicircleWithTopPlayers() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
         //   .background(upperLeaderboard)
            // Adjust height to control arc size
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Draw the background rectangle
//        drawRect(
//            color = Color(0xFF00BCD4),
//            size = Size(canvasWidth, canvasHeight)
//        )
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3F51B5), // Top color
                Color(0xFF673AB7)  // Bottom color
            )
        )

        // Draw inverted arc with same color to 'cut' the shape
        drawArc(
            brush = gradientBrush, // match background to simulate cut
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(0f, -canvasWidth / 2f),
            size = Size(canvasWidth, canvasWidth)
        )
    }
    }


    // Top 3 players go here





@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LeaderboardTopBackgroundPreview() {
    InvertedSemicircleWithTopPlayers()
}

