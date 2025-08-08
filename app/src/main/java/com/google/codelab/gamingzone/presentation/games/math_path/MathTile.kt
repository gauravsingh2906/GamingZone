package com.google.codelab.gamingzone.presentation.games.math_path

data class MathTile(
    val row: Int,
    val col: Int,
    val value: Int,
    val isSelected: Boolean = false
)
