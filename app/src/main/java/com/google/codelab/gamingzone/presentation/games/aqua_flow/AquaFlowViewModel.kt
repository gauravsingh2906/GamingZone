package com.google.codelab.gamingzone.presentation.games.aqua_flow

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AquaFlowViewModel : ViewModel() {

    private val _state = mutableStateOf(AquaFlowState())
    val state: State<AquaFlowState> = _state

    init {
        loadLevel1()
    }

    private fun loadLevel1() {
        val grid = MutableList(6) { y ->
            MutableList(6) { x ->
                Tile(x, y)
            }
        }.toMutableList()

        grid[0][0] = Tile(0, 0, TileType.SOURCE, WaterColor.BLUE)
        grid[0][1] = Tile(1, 0, TileType.PIPE)
        grid[0][2] = Tile(2, 0, TileType.PIPE)
        grid[0][3] = Tile(3, 0, TileType.PIPE)
        grid[0][4] = Tile(4, 0, TileType.TANK)

        _state.value = AquaFlowState(
            grid = grid,
            sourcePosition = 0 to 0
        )
    }

    fun rotatePipeAt(x: Int, y: Int) {
        val current = _state.value.grid[y][x]
        if (current.type != TileType.PIPE) return

        val newDirection = when (current.pipeDirection) {
            PipeDirection.HORIZONTAL -> PipeDirection.VERTICAL
            PipeDirection.VERTICAL -> PipeDirection.CORNER_TL
            PipeDirection.CORNER_TL -> PipeDirection.CORNER_TR
            PipeDirection.CORNER_TR -> PipeDirection.CORNER_BL
            PipeDirection.CORNER_BL -> PipeDirection.CORNER_BR
            PipeDirection.CORNER_BR -> PipeDirection.HORIZONTAL
            else -> PipeDirection.HORIZONTAL
        }

        val updated = current.copy(pipeDirection = newDirection)
        val newGrid = _state.value.grid.map { row -> row.toMutableList() }
        newGrid[y][x] = updated
        _state.value = _state.value.copy(grid = newGrid)
    }


    fun simulateFlow() {
        val newGrid = state.value.grid.map { it.toMutableList() }

        // Simulate water flow through the first row
        for (x in 1..4) {
            val prev = newGrid[0][x - 1]
            val current = newGrid[0][x]

            if (prev.type != TileType.EMPTY && current.type != TileType.EMPTY) {
                newGrid[0][x] = current.copy(isFilled = true, color = prev.color)
            }
        }

        val tank = newGrid[0][4]
        _state.value = state.value.copy(
            grid = newGrid,
            tankFilled = tank.isFilled
        )
    }
}
