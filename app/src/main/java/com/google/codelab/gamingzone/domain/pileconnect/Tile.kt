package com.google.codelab.gamingzone.domain.pileconnect

import androidx.compose.ui.graphics.Color

data class Tile(
    val row: Int,
    val col: Int,
    val color: Color? = null, // null for empty
    val isConnected: Boolean = false
)
