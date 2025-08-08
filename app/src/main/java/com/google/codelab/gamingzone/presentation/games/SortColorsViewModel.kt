package com.google.codelab.gamingzone.presentation.games

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Tube(val colors: List<Color>)

data class TubeFlightAnim(
    val tube: Tube,
    val from: Offset,
    val to: Offset,
    val colorToPour: Color,
    val pourAmount: Int,
    val fromIdx: Int,
    val toIdx: Int
)

class SortColorsViewModel : ViewModel() {
    var tubes by mutableStateOf(
        value = listOf(
            Tube(listOf(Color.Yellow, Color.Green, Color.Yellow, Color.Green)),
            Tube(listOf(Color.Red, Color.Blue, Color.Red, Color.Blue)),
            Tube(listOf()),
            Tube(listOf()),
            Tube(listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue))
        )
    )
        private set

    var selectedTube by mutableStateOf<Int?>(null)
        private set
    var isPouring by mutableStateOf(false)
        private set
    var flightAnim by mutableStateOf<TubeFlightAnim?>(null)
        private set

    fun onTubeClick(index: Int, tubePositions: List<Offset>) {
        if (isPouring) return
        if (selectedTube == null) {
            if (tubes[index].colors.isNotEmpty()) selectedTube = index
        } else if (selectedTube == index) {
            selectedTube = null
        } else {
            tryPour(selectedTube!!, index, tubePositions)
        }
    }

    private fun tryPour(from: Int, to: Int, tubePositions: List<Offset>) {
        val fromColors = tubes[from].colors
        val toColors = tubes[to].colors

        if (fromColors.isEmpty() || toColors.size >= 4) {
            selectedTube = null
            return
        }
        val colorToPour = fromColors.last()
        val countToPour = fromColors.reversed().takeWhile { it == colorToPour }.size
        val spaceLeft = 4 - toColors.size
        if (toColors.isNotEmpty() && toColors.last() != colorToPour) {
            selectedTube = null
            return
        }
        val actualPour = minOf(countToPour, spaceLeft)
        isPouring = true
        val fromOffset = tubePositions[from]
        val toOffset = tubePositions[to]
        flightAnim = TubeFlightAnim(
            tube = tubes[from],
            from = fromOffset,
            to = toOffset,
            colorToPour = colorToPour,
            pourAmount = actualPour,
            fromIdx = from,
            toIdx = to
        )
        viewModelScope.launch {
            delay(1550) // Matching total animation duration
            tubes = tubes.mapIndexed { i, t ->
                when (i) {
                    from -> Tube(t.colors.dropLast(actualPour))
                    to -> Tube(t.colors + List(actualPour) { colorToPour })
                    else -> t
                }
            }
            flightAnim = null
            selectedTube = null
            isPouring = false
        }
    }
}