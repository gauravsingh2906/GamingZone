package com.google.codelab.gamingzone.presentation.navigation

import android.annotation.SuppressLint
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import kotlinx.serialization.Serializable


@SuppressLint("UnsafeOptInUsageError")
@Serializable
sealed interface Routes {


    @Serializable
    data object HomeScreen: Routes

    @Serializable
    data class GameDetailScreen(val gameId: String): Routes

    @Serializable
    data class SudokuScreen(val game: String): Routes


    @Serializable
    data class ColorRaceScreen(val gameId: String, val difficulty: String): Routes

    @Serializable
    data class ChessScreen(val game: String, val difficulty: String): Routes

    @Serializable
    data object LeaderBoardScreen : Routes

    @Serializable
    data object ProfileScreen : Routes
}