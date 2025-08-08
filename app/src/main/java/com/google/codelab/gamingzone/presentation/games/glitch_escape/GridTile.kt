package com.google.codelab.gamingzone.presentation.games.glitch_escape

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

data class GridTile(
    val x: Int,
    val y: Int,
    val isSolid: Boolean = true,
    val isGlitching: Boolean = false
)

enum class Screen {
    MENU, DIFFICULTY, LEVEL_SELECT, GAME, GAME_OVER, VICTORY
}





data class Player(
    var x: Int,
    var y: Int
)

// --- Enemy Definition ---
data class Enemy(
    var x: Int,
    var y: Int,
    val path: List<Pair<Int, Int>>, // predefined path to follow
    var pathIndex: Int = 0
)

data class AnimatedEnemy(
    var x: Int,
    var y: Int,
    val path: List<Pair<Int, Int>>,
    var pathIndex: Int = 0,
    val offsetX: Animatable<Float, AnimationVector1D>,
    val offsetY: Animatable<Float, AnimationVector1D>
)


// --- GameState ---
data class GameState(
    val grid: List<GridTile>,
    val player: Player,
    val enemies: List<AnimatedEnemy>,
    val level: Int = 1,
    val steps: Int = 0,
    val timeElapsed: Int = 0, // seconds
    val isGameOver: Boolean = false,
    val isGameWon: Boolean = false
)
