package com.google.codelab.gamingzone.tobe

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color


data class Tube(
    val id: Int,
    val balls: MutableList<Color>
) {
    val isEmpty: Boolean get() = balls.isEmpty()
    val isFull: Boolean get() = balls.size >= MAX_CAPACITY
    val topColor: Color? get() = balls.lastOrNull()
    val canAdd: Boolean get() = balls.size < MAX_CAPACITY

    companion object {
        const val MAX_CAPACITY = 4
    }

}

data class BallTransferAnimation(
    val color: Color,
    val fromTubeIndex: Int,
    val toTubeIndex: Int,
    val startTimeMillis: Long = System.currentTimeMillis()
)



data class GameState(
    val tubes: List<Tube> = emptyList(),
    val isPouring: Boolean = false,
    val pourAnimation: PourAnimationData? = null,
    val selectedTubeIndex: Int? = null,
    val level: Int = 1,
    val won: Boolean = false,
)

data class PourAnimationData(
    val fromIndex: Int,
    val toIndex: Int,
    val color: Color
)

sealed class GameEvent {
    data class TubeClicked(val index: Int) : GameEvent()

    object NextLevel : GameEvent()
//
//    object Undo : GameEvent()
//    object Redo : GameEvent()

    data object RestartGame : GameEvent()
}



