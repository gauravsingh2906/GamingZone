package com.google.codelab.gamingzone.tobe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TubeViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val maxTubeCapacity = 4
    }

    private val _tubeStates = MutableStateFlow(
        listOf(
            listOf(Color.Red, Color.Green),
            listOf(Color.Yellow),
            emptyList(),
        )
    )
    val tubeStates = _tubeStates.asStateFlow()

    private val _selectedTubeIndex = MutableStateFlow<Int?>(null)
    val selectedTubeIndex = _selectedTubeIndex.asStateFlow()

    fun selectTube(index: Int) {
        _selectedTubeIndex.value = index
    }

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state


    // Undo/Redo history
    private val undoStack = ArrayDeque<List<Tube>>()
    private val redoStack = ArrayDeque<List<Tube>>()

    var ballTransferAnimation by mutableStateOf<BallTransferAnimation?>(null)

    fun triggerBallTransferAnimation(color: Color, fromIndex: Int, toIndex: Int) {
        ballTransferAnimation = BallTransferAnimation(color, fromIndex, toIndex)
    }

    // Game Event Handler
    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.TubeClicked -> handleTubeClick(event.index)
            is GameEvent.RestartGame -> restartGame()
//            is GameEvent.Undo -> undo()
//            is GameEvent.Redo -> redo()
            is GameEvent.NextLevel -> goToNextLevel()
        }
    }

    private fun handleTubeClick(index: Int) {
        val currentState = _state.value.selectedTubeIndex

        if (currentState == null) {
            // Select the tube
            _state.value = _state.value.copy(selectedTubeIndex = index)
        } else {
            transferBall(currentState, index)
            _state.value = _state.value.copy(selectedTubeIndex = null)
        }
    }

    private fun transferBall(fromIndex: Int, toIndex: Int) {
        if (fromIndex == toIndex) return

        val tubes = _state.value.tubes.toMutableList()
        val fromTube = tubes[fromIndex]
        val toTube = tubes[toIndex]

        if (fromTube.isEmpty || !toTube.canAdd) return

        val movingBall = fromTube.topColor!!

        // Ball movement condition (top-to-top & same color or empty)
        if (toTube.isEmpty || toTube.topColor == movingBall) {
            triggerBallTransferAnimation(
                color = movingBall,
                fromIndex = fromIndex,
                toIndex = toIndex
            )
            val updatedFrom = fromTube.copy(balls = fromTube.balls.dropLast(1).toMutableList())
            val updatedTo = toTube.copy(balls = toTube.balls.toMutableList().apply { add(movingBall) })

            tubes[fromIndex] = updatedFrom
            tubes[toIndex] = updatedTo


            _state.value = _state.value.copy(tubes = tubes)
            checkWinCondition(tubes)
        }
    }

    private fun checkWinCondition(tubes: List<Tube>) {
        val isWon = tubes.all { it.balls.isEmpty() || (it.balls.size == Tube.MAX_CAPACITY && it.balls.distinct().size == 1) }

        if (isWon) {
            _state.value = _state.value.copy(won = true)
        }
    }

    private fun restartGame() {
        val currentLevel = _state.value.level
        val newTubes = generateLevel(currentLevel)
        _state.value = GameState(tubes = newTubes,level = currentLevel)
    }

    private fun goToNextLevel() {
        val nextLevel = _state.value.level + 1
        val newTubes = generateLevel(nextLevel)
        _state.value = GameState(tubes = newTubes,level = nextLevel)
    }

    private fun generateLevel(level: Int): List<Tube> {
        val baseColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta)
        val levelColors = baseColors.take(level + 2) // Increase colors with level
        val allBalls = levelColors.flatMap { color -> List(Tube.MAX_CAPACITY) { color } }.shuffled()

        val tubes = allBalls.chunked(Tube.MAX_CAPACITY).mapIndexed { index, chunk ->
            Tube(id = index, balls = chunk.toMutableList()) // ✅ This should now work fine
        }.toMutableList()

        // Add 2 empty tubes
        repeat(2) {
            tubes.add(Tube(id = tubes.size, balls = mutableListOf()))
        }

        return tubes
    }
}





//private fun saveForUndo(currentTubes: List<Tube>) {
//    undoStack.addLast(currentTubes.map { it.copy(balls = it.balls.toMutableList()) })
//    redoStack.clear() // Invalidate redo stack
//}
//
//private fun undo() {
//    if (undoStack.isNotEmpty()) {
//        redoStack.addLast(_state.value.tubes.map { it.copy(balls = it.balls.toMutableList()) })
//        val previous = undoStack.removeLast()
//        _state.value = _state.value.copy(tubes = previous, selectedTubeIndex = null, won = false)
//    }
//}
//
//private fun redo() {
//    if (redoStack.isNotEmpty()) {
//        undoStack.addLast(_state.value.tubes.map { it.copy(balls = it.balls.toMutableList()) })
//        val next = redoStack.removeLast()
//        _state.value = _state.value.copy(tubes = next, selectedTubeIndex = null, won = false)
//    }
//}
//
//private fun checkWin(tubes: List<Tube>): Boolean {
//    return tubes.all { it.isEmpty || (it.balls.size == 4 && it.balls.distinct().size == 1) }
//}
//
//private fun restartGame() {
//    val newTubes = generateLevel()
//    undoStack.clear()
//    redoStack.clear()
//    _state.value = GameState(tubes = newTubes)
//}
//
//fun resetGame() {
//    restartGame()
//}
//
////    private fun loadLevel(level: Int) {
////        val tubes = generateLevel(level)
////        undoStack.clear()
////        redoStack.clear()
////        _state.value = GameState(tubes = tubes)
////    }
//
//private fun generateLevel(level: Int = 1): List<Tube> {
//    val baseColors = when (level) {
//        1 -> listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
//        2 -> listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)
//        3 -> listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan)
//        else -> listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
//    }
//
//    val allBalls = baseColors.flatMap { color -> List(maxTubeCapacity) { color } }.shuffled()
//
//    val tubes = allBalls.chunked(maxTubeCapacity).mapIndexed { index, chunk ->
//        Tube(index, chunk.toMutableList())
//    }.toMutableList()
//
//    repeat(2) {
//        tubes.add(Tube(tubes.size, mutableListOf()))
//    }
//
//    return tubes
//}
//}
