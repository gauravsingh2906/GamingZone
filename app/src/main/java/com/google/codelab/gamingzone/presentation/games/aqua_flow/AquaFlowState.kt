package com.google.codelab.gamingzone.presentation.games.aqua_flow

data class AquaFlowState(
    val grid: List<List<Tile>> = emptyList(),
    val sourcePosition: Pair<Int, Int>? = null,
    val tankFilled: Boolean = false
)

