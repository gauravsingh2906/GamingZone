package com.google.codelab.gamingzone.presentation.feature_code_runner


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CodeRunnerViewModel : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState

    private val gravity = 0.6f
    private val jumpPower = -12f
    private val groundY = 0f

    init {
        startGameLoop()
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                updatePhysics()
                delay(16) // ~60 FPS
            }
        }
    }

    private fun updatePhysics() {
        val current = _playerState.value

        if (!current.isOnGround) {
            val newVelocity = current.velocityY + gravity
            var newY = current.y + newVelocity

            var grounded = false
            if (newY >= groundY) {
                newY = groundY
                grounded = true
            }

            _playerState.value = current.copy(
                y = newY,
                velocityY = newVelocity,
                isOnGround = grounded,
                action = if (grounded) PlayerAction.RUN else current.action
            )
        }
    }

    fun jump() {
        val state = _playerState.value
        if (state.isOnGround) {
            _playerState.value = state.copy(
                velocityY = -15f,
                isOnGround = false,
                action = PlayerAction.JUMP
            )
        }
    }

    fun dash() {
        _playerState.value = _playerState.value.copy(
            action = PlayerAction.DASH
        )
    }

    fun teleport() {
        _playerState.value = _playerState.value.copy(
            x = _playerState.value.x + 200f,
            action = PlayerAction.TELEPORT
        )
    }
}
