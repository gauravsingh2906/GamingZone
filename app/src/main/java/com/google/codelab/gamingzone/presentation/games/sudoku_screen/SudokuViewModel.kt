package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.domain.sudoku.PuzzleGenerator
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.sampleGames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.all

@HiltViewModel
class SudokuViewModel @Inject constructor(
    private val generator: PuzzleGenerator,
    savedStateHandle: SavedStateHandle// No longer DefaultPuzzleGenerator directly
) : ViewModel() {

    private val _state = mutableStateOf(SudokuState())

    val state: State<SudokuState> = _state

    private val _event = MutableSharedFlow<SudokuGameEvent>()
    val event = _event.asSharedFlow()

    private var timerJob: Job? = null

    private val games = sampleGames // Your sampleGames list

    fun getGameById(id: String): GameItem? {
        return games.find { it.id == id }
    }

    init {
        val difficultyArg = savedStateHandle.get<String>("difficulty")
        val difficulty = difficultyArg?.let {
            Difficulty.valueOf(it)
        } ?: Difficulty.EASY // fallback

        _state.value = _state.value.copy(difficulty = difficulty)
        generatePuzzle()
        startTimer()
    }

    fun onAction(action: SudokuAction) {
        when (action) {
            is SudokuAction.SelectCell -> {
                _state.value = _state.value.copy(selectedCell = action.row to action.col)
            }

            is SudokuAction.EnterNumber -> {
                val selected = state.value.selectedCell ?: return
                val (row, col) = selected
                if (state.value.originalBoard[row][col].isFixed) return

                val boardCopy = state.value.board.map { it.toMutableList() }
                boardCopy[row][col] = Cell(row, col, action.number, false)

                val invalid = !isValidMove(boardCopy, row, col, action.number)
                val newMistakes = state.value.mistakes + if (invalid) 1 else 0

                _state.value = state.value.copy(
                    board = boardCopy,
                    mistakes = newMistakes,
                    invalidCells = if (invalid)
                        state.value.invalidCells + (row to col)
                    else
                        state.value.invalidCells - (row to col),
                    isGameOver = newMistakes >= 3
                )

                if (newMistakes >= 3) {
                    viewModelScope.launch { _event.emit(SudokuGameEvent.GameOver) }
                } else if (isPuzzleSolved(boardCopy)) {
                    _state.value = state.value.copy(isGameWon = true)
                    viewModelScope.launch { _event.emit(SudokuGameEvent.PuzzleSolved) }
                }
            }

            SudokuAction.UseHint -> {
                val currentState = _state.value
                val maxHints = 3
                if (currentState.hintsUsed >= maxHints) return

                val currentBoard = currentState.board
                val solution = currentState.solutionBoard

                // Find the first editable (non-fixed), empty or incorrect cell
                val hintCell = currentBoard.flatten().firstOrNull { cell ->
                    !cell.isFixed && (cell.value == 0 || cell.value != solution[cell.row][cell.col].value)
                }

                if (hintCell != null) {
                    val correctValue = solution[hintCell.row][hintCell.col].value

                    val updatedBoard = currentBoard.map { row ->
                        row.map { cell ->
                            if (cell.row == hintCell.row && cell.col == hintCell.col) {
                                cell.copy(
                                    value = correctValue,
                                    isHint = true
                                )
                            } else {
                                cell
                            }
                        }
                    }
           viewModelScope.launch {
               _state.value = currentState.copy(
                   board = updatedBoard,
                   hintsUsed = currentState.hintsUsed + 1
               )
               delay(1000L) // Hint highlight shows for 1 second

               val resetBoard = updatedBoard.map { row ->
                   row.map { it.copy(isHint = false) }
               }

               _state.value = _state.value.copy(board = resetBoard)
           }

                }
            }

            SudokuAction.RestartGame ->{
                generatePuzzle()
            }

            is SudokuAction.ChangeDifficulty -> {
                _state.value = _state.value.copy(difficulty = action.difficulty)
                generatePuzzle()
            }
        }

    }

    fun generatePuzzle() {
        val (puzzle,solution) = generator.generate(_state.value.difficulty!!)
        val cells = puzzle.mapIndexed { row, list ->
            list.mapIndexed { col, value ->
                Cell(
                    row, col, value, value != 0,
                )
            }
        }

        val solutionBoard = solution.mapIndexed { row, list ->
            list.mapIndexed { col, value ->
                Cell(
                    row, col, value, isFixed = true,
                )
            }
        }

        _state.value = SudokuState(
            board = cells,
            originalBoard = cells,
            solutionBoard = solutionBoard,
            difficulty = _state.value.difficulty,
            hintsUsed = 0,
            mistakes = 0,
            isGameOver = false,
            isGameWon = false,
            elapsedTime = 0,
            invalidCells = emptySet(),
            selectedCell = null,
            selectedNumber = null
        )
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _state.value = _state.value.copy(elapsedTime = _state.value.elapsedTime + 1)
            }
        }
    }

    private fun isValidMove(board: List<List<Cell>>, row: Int, col: Int, num: Int): Boolean {
        for (i in 0 until 9) {
            if (board[row][i].value == num && i != col) return false
            if (board[i][col].value == num && i != row) return false
        }

        val startRow = row - row % 3
        val startCol = col - col % 3
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val cell = board[startRow + i][startCol + j]
                if (cell.value == num && (startRow + i != row || startCol + j != col)) return false
            }
        }
        return true
    }

    private fun isPuzzleSolved(board: List<List<Cell>>): Boolean {
        return board.all { row -> row.all { it.value != 0 } } &&
                board.flatten().none { (row, col) ->
                    !isValidMove(board, row, col, board[row][col].value)
                }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}