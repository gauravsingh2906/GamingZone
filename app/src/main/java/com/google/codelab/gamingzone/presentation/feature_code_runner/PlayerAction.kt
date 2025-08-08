package com.google.codelab.gamingzone.presentation.feature_code_runner


enum class PlayerAction {
    IDLE, RUN, JUMP, DASH, TELEPORT
}

data class PlayerState(
    val x: Float = 100f,
    val y: Float = 0f,
    val velocityY: Float = 0f,
    val action: PlayerAction = PlayerAction.RUN,
    val isOnGround: Boolean = true
)
