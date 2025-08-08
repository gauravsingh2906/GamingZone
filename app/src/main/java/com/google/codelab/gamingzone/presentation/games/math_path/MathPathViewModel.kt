package com.google.codelab.gamingzone.presentation.games.math_path

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.presentation.game_detail_screen.GameMode
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuGameEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MathPathViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mode = GameMode.valueOf(
        savedStateHandle["mode"] ?: GameMode.LEVEL.name
    )

    private val difficulty = if (mode == GameMode.TIMED)
        Difficulty.valueOf(savedStateHandle["difficulty"] ?: Difficulty.EASY.name)
    else null

    private val _state = mutableStateOf(MathPathState(

    ))
    val state: State<MathPathState> = _state

    private val _event = MutableSharedFlow<MathGridEvent>()
    val event = _event.asSharedFlow()

    init {
        generateNewGrid()
    }

    private fun generateNewGrid() {
        val gridSize = when {
            mode == GameMode.LEVEL && _state.value.level >= 10 -> 6
            mode == GameMode.LEVEL && _state.value.level >= 5 -> 5
            else -> 4
        }

        val grid = List(gridSize) { row ->
            List(gridSize) { col ->
                MathTile(row, col, (1..9).random())
            }
        }

        val target = when (mode) {
            GameMode.LEVEL -> {
                val base = 10 + (_state.value.level * 2)
                (base..base + 10).random()
            }

            GameMode.TIMED -> {
                val range = when (difficulty) {
                    Difficulty.EASY -> 10..20
                    Difficulty.MEDIUM -> 20..35
                    Difficulty.HARD -> 35..50
                    null -> 10..20
                }
                range.random()
            }
        }

        _state.value = _state.value.copy(
            grid = grid,
            targetSum = target,
            selectedTiles = emptyList(),
            isGameOver = false,
        )
    }

    fun onAction(event: MathGridEvent) {
        when (event) {
            is MathGridEvent.SelectTile -> {

                val alreadySelected = _state.value.selectedTiles.any {
                    it.row == event.tile.row && it.col == event.tile.col
                }
                if (!alreadySelected) {
                    _state.value = _state.value.copy(
                        selectedTiles = _state.value.selectedTiles + event.tile
                    )
                }
                val selectedSum = _state.value.selectedTiles.sumOf { it.value }
                if(selectedSum > _state.value.targetSum) {
                    _state.value = _state.value.copy(
                        streak = 0,
                        isGameOver = true,
                        selectedTiles = emptyList(),
                        level = 1
                    )
                    viewModelScope.launch {
                        _event.emit(MathGridEvent.GameOver)
                    }
                }
            }

            MathGridEvent.SubmitSelection -> {
                val selectedSum = _state.value.selectedTiles.sumOf { it.value }

                if (selectedSum == _state.value.targetSum) {
                    val scoreIncrease = 10 + _state.value.streak * 5

                    _state.value = _state.value.copy(
                        score = _state.value.score + scoreIncrease,
                        streak = _state.value.streak + 1,// in TIMED, we end the round
                        selectedTiles = emptyList(),
                        level = _state.value.level+1
                    )
//                    if (mode == GameMode.LEVEL) {
//                        _state.value = _state.value.copy(isGameOver = true)
//                    }
//                    _state.value = _state.value.copy(
//                        targetSum = _state.value.targetSum + 8,
//                        level = _state.value.level+1
//                    )
                    generateNewGrid()
                } else {
                    _state.value = _state.value.copy(
                        streak = 0,
                        isGameOver = true,
                        selectedTiles = emptyList(),
                        level = 2
                    )
                }
            }

            MathGridEvent.NextLevel -> {
                _state.value = _state.value.copy(level = _state.value.level + 1)
                generateNewGrid()
            }

            MathGridEvent.RestartGame -> {
                _state.value = _state.value.copy(
                   streak = 0,
                    score = 0,
                    isGameOver = false
                )
                generateNewGrid()
            }

            MathGridEvent.GameOver -> {
                _state.value = _state.value.copy(isGameOver = true)
            }

        }
    }

}

