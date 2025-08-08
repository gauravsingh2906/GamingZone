package com.google.codelab.gamingzone.presentation.games.math_path

sealed class MathGridEvent {
    data class SelectTile(val tile: MathTile) : MathGridEvent()
    object SubmitSelection : MathGridEvent()
    object RestartGame : MathGridEvent()
//    object ShuffleGrid : MathGridEvent()
    object NextLevel : MathGridEvent()
    object GameOver : MathGridEvent()
}
