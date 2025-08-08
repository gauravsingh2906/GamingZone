package com.google.codelab.gamingzone.presentation.games.math_path

sealed class MathPathEvent {
    data class TileTapped(val tile: MathTile) : MathPathEvent()
    object Submit : MathPathEvent()
    object Reset : MathPathEvent()
    object Tick : MathPathEvent()
}