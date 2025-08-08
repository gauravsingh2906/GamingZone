package com.google.codelab.gamingzone.presentation.games.fear_zone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sign


@HiltViewModel
class FearZoneViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(FearZoneState())
        private set

    private val gridSize = 5

    fun movePlayer(dx: Int, dy: Int) {
        if (state.isGameOver || state.isCompleted) return

        val (px, py) = state.playerPos
        val newPx = (px + dx).coerceIn(0, gridSize - 1)
        val newPy = (py + dy).coerceIn(0, gridSize - 1)
        val newPlayer = newPx to newPy

        val newLocks = if (newPlayer in state.lockTiles)
            state.unlockedLocks + newPlayer else state.unlockedLocks

        val newSafeTile = if (newLocks.size == state.lockTiles.size) 0 to 4 else state.safeTile

        val newTurn = state.turnCount + 1
        val newEnemy = if (newTurn % 2 == 0) moveEnemyToward(newPlayer, state.enemyPos) else state.enemyPos

        val newFear = when {
            newPlayer == newEnemy -> 1f
            else -> (state.fearLevel + 0.1f).coerceAtMost(1f)
        }

        val gameOver = newPlayer == newEnemy || newFear >= 1f
        val completed = newPlayer == newSafeTile

        state = state.copy(
            playerPos = newPlayer,
            unlockedLocks = newLocks,
            safeTile = newSafeTile,
            turnCount = newTurn,
            enemyPos = newEnemy,
            fearLevel = newFear,
            isFlicker = newTurn % 6 < 3,
            isGameOver = gameOver,
            isCompleted = completed
        )
    }

    fun resetLevel() {
        state = FearZoneState()
    }
    fun moveEnemyToward(player: Pair<Int, Int>, enemy: Pair<Int, Int>): Pair<Int, Int> {
        val (px, py) = player
        val (ex, ey) = enemy
        val dx = px - ex
        val dy = py - ey

        return when {
            abs(dx) > abs(dy) -> (ex + dx.sign) to ey // Move horizontally
            else -> ex to (ey + dy.sign)              // Move vertically
        }
    }

}



