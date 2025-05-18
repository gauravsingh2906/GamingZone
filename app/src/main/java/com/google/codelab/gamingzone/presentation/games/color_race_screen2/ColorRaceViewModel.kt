package com.google.codelab.gamingzone.presentation.games.color_race_screen2

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject




class ColorRaceViewModel : ViewModel() {

    private val _state = MutableStateFlow(ColorRaceState())
    val state: StateFlow<ColorRaceState> = _state

    fun onEvent(event: ColorRaceEvent) {
        when (event) {
            is ColorRaceEvent.StartGame -> startGame()
            is ColorRaceEvent.ColorClicked -> checkUserInput(event.color)
            is ColorRaceEvent.DismissGameOverDialog -> dismissGameOverDialog()
        }
    }

    private fun startGame() {
        _state.update {
            it.copy(
                sequence = generateSequence(it.level),
                userInput = emptyList(),
                isGameOver = false,
                isShowingSequence = true,
                currentBlinkIndex = -1
            )
        }
        showSequence()
    }

    private fun generateSequence(level: Int): List<ColorOption> {
        return List(level) {
            ColorOption.values().random()
        }
    }

    private fun showSequence() {
        viewModelScope.launch {
            state.value.sequence.forEachIndexed { index, _ ->
                updateSequenceBlink(index)
                delay(500L) // Blink 1 color per second
            }
            _state.update { it.copy(isShowingSequence = false, currentBlinkIndex = -1) }
        }
    }

    private fun updateSequenceBlink(index: Int) {
        _state.update {
            it.copy(currentBlinkIndex = index)
        }
    }

    private fun checkUserInput(color: ColorOption) {
        val currentState = state.value
        val updatedInput = currentState.userInput + color

        if (color != currentState.sequence[updatedInput.lastIndex]) {
            _state.update {
                it.copy(
                    isGameOver = true,
                    showGameOverDialog = true
                )
            }
            return
        }

        if (updatedInput.size == currentState.sequence.size) {
            _state.update {
                it.copy(
                    level = it.level + 1,
                    userInput = emptyList(),
                    isShowingSequence = true
                )
            }
            delayThenShowNextSequence()
        } else {
            _state.update {
                it.copy(userInput = updatedInput)
            }
        }
    }

    private fun delayThenShowNextSequence() {
        viewModelScope.launch {
            delay(1000L)
            _state.update {
                it.copy(
                    sequence = generateSequence(it.level),
                    currentBlinkIndex = -1
                )
            }
            showSequence()
        }
    }

    private fun dismissGameOverDialog() {
        _state.update {
            it.copy(showGameOverDialog = false)
        }
    }
}


