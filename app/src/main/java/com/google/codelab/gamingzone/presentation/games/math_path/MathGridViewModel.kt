package com.google.codelab.gamingzone.presentation.games.math_path
//
//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update
//import kotlin.random.Random
//
//class MathGridViewModel : ViewModel() {
//
//    private val _state = MutableStateFlow(createInitialState())
//    val state: StateFlow<MathPathState> = _state
//
//    fun onEvent(event: MathGridEvent) {
//        when (event) {
//            is MathGridEvent.TileClicked -> {
//                _state.update { current ->
//                    if (current.selectedTiles.contains(event.tile)) {
//                        current.copy(selectedTiles = current.selectedTiles - event.tile)
//                    } else {
//                        current.copy(selectedTiles = current.selectedTiles + event.tile)
//                    }
//                }
//            }
//
//            MathGridEvent.SubmitSelection -> {
//                _state.update { current ->
//                    val sum = current.selectedTiles.sumOf { it.value }
//                    val newScore = if (sum == current.targetSum) current.score + 10 else current.score
//                    val newMoves = current.movesLeft - 1
//
//                    val updatedTiles = current.grid.map { row ->
//                        row.map { tile ->
//                            if (current.selectedTiles.any { it.row == tile.row && it.col == tile.col }) {
//                                tile.copy(isSelected = false)
//                            } else tile
//                        }
//                    }
//
//                    current.copy(
//                        score = newScore,
//                        movesLeft = newMoves,
//                        selectedTiles = emptyList(),
//                        isGameOver = newMoves <= 0,
//                        grid = updatedTiles,
//                        targetSum = generateNewTarget(updatedTiles)
//                    )
//                }
//            }
//
//            MathGridEvent.RestartGame -> {
//                _state.value = createInitialState()
//            }
//
//            MathGridEvent.ShuffleGrid -> {
//                _state.update {
//                    val shuffledGrid = shuffleGridValues(it.grid)
//                    it.copy(
//                        grid = shuffledGrid,
//                        targetSum = generateNewTarget(shuffledGrid)
//                    )
//                }
//            }
//        }
//    }
//
//    private fun createInitialState(): MathPathState {
//        val grid = generateRandomGrid(5)
//        return MathPathState(
//            grid = grid,
//            targetSum = generateNewTarget(grid)
//        )
//    }
//
//    private fun generateRandomGrid(size: Int): List<List<MathTile>> {
//        return List(size) { row ->
//            List(size) { col ->
//                MathTile(
//                    row = row,
//                    col = col,
//                    value = Random.nextInt(1, 20)
//                )
//            }
//        }
//    }
//
//    private fun generateNewTarget(grid: List<List<MathTile>>): Int {
//        val flat = grid.flatten().shuffled()
//        return flat.take(3).sumOf { it.value }
//    }
//
//    private fun shuffleGridValues(grid: List<List<MathTile>>): List<List<MathTile>> {
//        val values = grid.flatten().map { it.value }.shuffled()
//        var index = 0
//        return grid.mapIndexed { rowIdx, row ->
//            row.mapIndexed { colIdx, tile ->
//                tile.copy(value = values[index++])
//            }
//        }
//    }
//}
