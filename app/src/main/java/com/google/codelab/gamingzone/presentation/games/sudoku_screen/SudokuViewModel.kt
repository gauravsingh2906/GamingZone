package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.local.dao.UserProfileDao
import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity
import com.google.codelab.gamingzone.data.model.GameResult
import com.google.codelab.gamingzone.data.model.GameStats

import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.domain.sudoku.PuzzleGenerator
import com.google.codelab.gamingzone.presentation.games.trap_bot.DifficultyLevel
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
    private val dao: SudokuResultDao,
    savedStateHandle: SavedStateHandle// No longer DefaultPuzzleGenerator directly
) : ViewModel() {

    private val _state = mutableStateOf(SudokuState(isTimerRunning = true))
    val state: State<SudokuState> = _state

    private val _event = MutableSharedFlow<SudokuGameEvent>()
    val event = _event.asSharedFlow()

    private var timerJob: Job? = null

    private val games = sampleGames // Your sampleGames list

    fun getGameById(id: String): GameItem? {
        return games.find { it.id == id }
    }

    private var difficulty: Difficulty = Difficulty.EASY // default

  //  var xpEarned: Int = 0


    init {
        val difficultyArg = savedStateHandle.get<String>("difficulty")
        difficulty = try {
            Difficulty.valueOf(difficultyArg ?: Difficulty.EASY.name)
        } catch (e: IllegalArgumentException) {
            Difficulty.EASY
        }

        _state.value = _state.value.copy(difficulty = difficulty)

        generatePuzzle(difficulty)
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

                val correctValue = state.value.solutionBoard[row][col].value
                val invalid = action.number != correctValue

                //    val invalid = !isValidMove(boardCopy, row, col, action.number)
                val newMistakes = state.value.mistakes + if (invalid) 1 else 0

                _state.value = state.value.copy(
                    board = boardCopy,
                    mistakes = newMistakes,
                    invalidCells = if (invalid)
                        state.value.invalidCells + (row to col)
                    else
                        state.value.invalidCells - (row to col),
                    isGameOver = newMistakes == 3
                )

                if (newMistakes == 3) {
                    _state.value = state.value.copy(
                        mistakes = newMistakes,
                        isTimerRunning = false,
                        board = boardCopy,
                        xpEarned = if (difficulty == Difficulty.EASY) 5
                        else if (difficulty == Difficulty.MEDIUM) 10
                        else 15
                    )
                    _state.value = _state.value.copy(
                        isGameOver = true,
                        isTimerRunning = false
                    )
                    viewModelScope.launch {
                        delay(500) // Let UI update mistake before showing dialog
//                        xpEarned = when (difficulty) {
//                            Difficulty.EASY -> if (_state.value.isGameWon) 20 else 5
//                            Difficulty.MEDIUM -> if (_state.value.isGameWon) 35 else 10
//                            Difficulty.HARD -> if (_state.value.isGameWon) 50 else 15
//                        }
                        // the upcoming code is important
//                        val currentUser = userProfileRepository.getProfile()
//                        Log.d("All", "Profile $currentUser")
//                        if (currentUser != null) {
//                            val updatedProfile = updateAfterGame(
//                                currentUser = currentUser,
//                                result = GameResult(
//                                    gameName = "sudoku",
//                                    difficulty = difficulty,
//                                    isWin = false,
//                                    isDraw = false,
//                                    xpEarned = xpEarned
//                                )
//                            )
//                            Log.d("All", "Updated Profile $currentUser")
//                            userProfileRepository.updateProfile(updatedProfile) // <-- THIS IS THE SAVE
//                        }

                        saveSudokuResult()
                        _event.emit(SudokuGameEvent.GameOver)
                    }
                    return
                } else if (isPuzzleSolved(boardCopy)) {
                    _state.value = _state.value.copy(
                        isGameWon = true,
                        isTimerRunning = false,
                        xpEarned =  when (difficulty) {
                            Difficulty.EASY ->  20
                            Difficulty.MEDIUM ->  35
                            Difficulty.HARD ->  50
                        }
                    )
                    viewModelScope.launch {
//                        xpEarned = when (difficulty) {
//                            Difficulty.EASY -> if (_state.value.isGameWon) 20 else 5
//                            Difficulty.MEDIUM -> if (_state.value.isGameWon) 35 else 10
//                            Difficulty.HARD -> if (_state.value.isGameWon) 50 else 15
//                        }
//                        val currentUser = userProfileRepository.getProfile()
//                        if (currentUser != null) {
//                            val updatedProfile = updateAfterGame(
//                                currentUser = currentUser,
//                                result = GameResult(
//                                    gameName = "sudoku",
//                                    difficulty = difficulty,
//                                    isWin = true,
//                                    isDraw = false,
//                                    xpEarned = xpEarned
//                                )
//                            )
//                            userProfileRepository.updateProfile(updatedProfile) // <-- THIS IS THE SAVE
//                        }

                        saveSudokuResult()
                        _event.emit(SudokuGameEvent.PuzzleSolved)
                    }
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
                                cell.copy(value = correctValue, isHint = true)
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

                val boardCopy = state.value.board.map { it.toMutableList() }

                if (isPuzzleSolved(boardCopy)) {
                    _state.value = _state.value.copy(
                        isGameWon = true,
                        isTimerRunning = false,
                        xpEarned = when (difficulty) {
                            Difficulty.EASY ->  20
                            Difficulty.MEDIUM ->  35
                            Difficulty.HARD ->  50
                        }
                    )


                    val xpEarned = when (difficulty) {
                        Difficulty.EASY -> if (_state.value.isGameWon) 20 else 5
                        Difficulty.MEDIUM -> if (_state.value.isGameWon) 35 else 10
                        Difficulty.HARD -> if (_state.value.isGameWon) 50 else 15
                    }
                    viewModelScope.launch {
//                        updateAfterGame(
//                            currentUser = UserProfileEntity(
//                                currentXP = 34,
//                                xpToNextLevel = 54,
//                                gamesPlayed = 43,
//                                highestScore = 43,
//                                totalPlayTimeInMinutes = 23,
//                                renameTickets = 1,
//                                username = "Gaurav",
//                                avatarId = 1
//                            ),
//                            result = GameResult(
//                                gameName = "sudoku",
//                                difficulty = difficulty,
//                                isWin = true,
//                                isDraw = false,
//                                xpEarned = xpEarned
//                            )
//                        )
                        saveSudokuResult()
                        _event.emit(SudokuGameEvent.PuzzleSolved)
                    }

                }
            }

            SudokuAction.RestartGame -> {
                generatePuzzle(difficulty)
            }

//            is SudokuAction.SetDifficulty -> {
//                _state.value = _state.value.copy(difficulty = action.difficulty)
//            }
        }
    }


    private suspend fun saveSudokuResult() {
        val currentState = _state.value

        val puzzleString =
            currentState.originalBoard.flatten().joinToString("") { it.value.toString() }
        val userSolutionString =
            currentState.board.flatten().joinToString("") { it.value.toString() }

        val result = SudokuResultEntity(
            difficulty = currentState.difficulty!!.name,
            puzzle = puzzleString,
            userSolution = userSolutionString,
            hintsUsed = currentState.hintsUsed,
            mistakesMade = currentState.mistakes,
            timeTakenSeconds = currentState.elapsedTime,
            xpEarned = currentState.xpEarned,
        )

        _state.value.elapsedTime = currentState.elapsedTime

        dao.insertResult(result)
    }


    fun generatePuzzle(difficulty: Difficulty) {
        val (puzzle, solution) = generator.generate(difficulty)
        val cells = puzzle.mapIndexed { row, list ->
            list.mapIndexed { col, value ->
                Cell(row, col, value, value != 0,)
            }
        }

        val solutionBoard = solution.mapIndexed { row, list ->
            list.mapIndexed { col, value ->
                Cell(
                    row, col, value, isFixed = true,
                )
            }
        }

        Log.d("sol", solutionBoard.toString())

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
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _state.value
                if (currentState.isTimerRunning) {
                    _state.value = currentState.copy(elapsedTime = currentState.elapsedTime + 1)
                } else {
                    break // Stop loop when timer stops
                }
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

//    private fun isPuzzleSolved(board: List<List<Cell>>): Boolean {
//        return board.all { row -> row.all { it.value != 0 } } &&
//                board.flatten().none { (row, col) ->
//                    !isValidMove(board, row, col, board[row][col].value)
//                }
//    }

    fun updateAfterGame(
        currentUser: UserProfileEntity,
        result: GameResult
    ): UserProfileEntity {

        val newTotalGames = currentUser.gamesPlayed + 1
        val newXp = currentUser.currentXP + result.xpEarned
        var newLevel = currentUser.level
        var newXpToNewLevel = currentUser.xpToNextLevel
        var remainingXp = newXp

        while (remainingXp >= newXpToNewLevel) {
            remainingXp -= newXpToNewLevel
            newLevel++
            newXpToNewLevel += 50
        }

        val finalXp = remainingXp

        val currentGameStats = currentUser.gameStats[result.gameName]

        val updatedGameStats = currentGameStats?.copy(
            gamesPlayed = currentGameStats.gamesPlayed + 1,
            wins = if (result.isWin == true) currentGameStats.wins + 1 else currentGameStats.wins,
            losses = if (result.isWin == false) currentGameStats.losses + 1 else currentGameStats.losses,
            totalDraws = if (result.isDraw) currentGameStats.totalDraws + 1 else currentGameStats.totalDraws,
            xpEarned = newXp,
            easyGamesPlayed = if (result.difficulty == Difficulty.EASY) currentGameStats.easyGamesPlayed + 1 else currentGameStats.easyGamesPlayed,
            mediumGamesPlayed = if (result.difficulty == Difficulty.MEDIUM) currentGameStats.mediumGamesPlayed + 1 else currentGameStats.mediumGamesPlayed,
            hardGamesPlayed = if (result.difficulty == Difficulty.HARD) currentGameStats.hardGamesPlayed + 1 else currentGameStats.hardGamesPlayed
        ) ?: GameStats( // if game has no stats yet
            gameName = result.gameName,
            gamesPlayed = 1,
            wins = if (result.isWin == true) 1 else 0,
            losses = if (result.isWin == false) 1 else 0,
            totalDraws = if (result.isDraw) 1 else 0,
            easyGamesPlayed = if (result.difficulty == Difficulty.EASY) 1 else 0,
            mediumGamesPlayed = if (result.difficulty == Difficulty.MEDIUM) 1 else 0,
            hardGamesPlayed = if (result.difficulty == Difficulty.HARD) 1 else 0
        )

        val updatedGameStatsMap = currentUser.gameStats.toMutableMap()

        updatedGameStatsMap[result.gameName] = updatedGameStats


        return currentUser.copy(
            gamesPlayed = newTotalGames,
            currentXP = finalXp,
            level = newLevel,
            gameStats = updatedGameStatsMap
        )


    }

    private fun isPuzzleSolved(board: List<List<Cell>>): Boolean {
        val solution = state.value.solutionBoard
        return board.flatten().all { cell ->
            cell.value == solution[cell.row][cell.col].value
        }
    }


    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}