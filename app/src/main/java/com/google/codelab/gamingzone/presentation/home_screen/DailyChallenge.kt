package com.google.codelab.gamingzone.presentation.home_screen

import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.navigation.Routes
import java.util.UUID

data class DailyChallenge(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val gameRoute: Routes,
    val timeLimitSeconds: Int? = null,
    val difficulty: Difficulty? = null
)
