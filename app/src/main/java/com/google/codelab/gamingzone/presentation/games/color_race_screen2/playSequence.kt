package com.google.codelab.gamingzone.presentation.games.color_race_screen2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun playSequence(
    sequence: List<Int>,
    updateGameSequence: (List<Int>) -> Unit,
    coroutineScope: CoroutineScope,
    updateShowColor: (Int) -> Unit,
    updateUserTurn: (Boolean) -> Unit
) {
    val newSequence = sequence + (0..3).random()
    updateGameSequence(newSequence)

    coroutineScope.launch {
        delay(500)
        for (color in newSequence) {
            updateShowColor(color)
            delay(500)
            updateShowColor(-1)
            delay(300)
        }
        updateUserTurn(true)
    }
}
