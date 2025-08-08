package com.google.codelab.gamingzone.presentation.games.aqua_flow

data class Tile(
    val x: Int,
    val y: Int,
    val type: TileType = TileType.EMPTY,
    val color: WaterColor = WaterColor.NONE,
    val isFilled: Boolean = false,
    val pipeDirection: PipeDirection = PipeDirection.NONE
)

