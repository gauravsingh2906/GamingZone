package com.google.codelab.gamingzone.presentation.navigation

import android.annotation.SuppressLint
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.games.trap_bot.DifficultyLevel
import kotlinx.serialization.Serializable


@SuppressLint("UnsafeOptInUsageError")
@Serializable
sealed interface Routes {


    @Serializable
    data object HomeScreen: Routes

    @Serializable
    data class GameDetailScreen(val gameId: String): Routes

    @Serializable
    data class SudokuScreen(val game: String, val difficulty: String): Routes


    @Serializable
    data class ColorRaceScreen(val gameId: String, val difficulty: String): Routes

    @Serializable
    data class ChessScreen(val game: String, val difficulty: String): Routes

    @Serializable
    data object DifficultySelectionScreen : Routes


    @Serializable
    data class TrapBotGameScreen(val difficulty: DifficultyLevel): Routes


    @Serializable
    data object LeaderBoardScreen : Routes

    @Serializable
    data object ProfileScreen : Routes

    @Serializable
    data object GameCategoryScreen : Routes

    @Serializable
    data object PileConnectScreen : Routes
}