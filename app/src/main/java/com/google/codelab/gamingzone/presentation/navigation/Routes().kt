package com.google.codelab.gamingzone.presentation.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

const val DifficultyScreenRoute = "difficulty_screen"
const val TrapBotGameRoute = "trap_bot_game?difficulty={difficulty}"
const val GameDetailScreenRoute = "game_detail_screen"
const val GameDetailScreenRouteWithArg = "$GameDetailScreenRoute/{gameId}"
const val SudokuScreenRoute = "sudoku_screen?gameId={gameId}&difficulty={difficulty}"
const val ChessScreenRoute = "chess_screen?gameId={gameId}&difficulty={difficulty}"
const val MathScreenRoute = "math_screen?gameId={gameId}&difficulty={difficulty}&mode={mode}"

@SuppressLint("UnsafeOptInUsageError")
@Serializable
sealed class Routes {


    @Serializable
    data object SplashScreen : Routes()


    @Serializable
    data object HomeScreen: Routes()

//    @Serializable
//    data class GameDetailScreen(val gameId: String): Routes()
//
//    @Serializable
//    data class SudokuScreen(val game: String, val difficulty: String): Routes()


    @Serializable
    data object ColorRaceScreen: Routes()

//    @Serializable
//    data class ChessScreen(val game: String, val difficulty: String): Routes()

//    @Serializable
//    data object DifficultySelectionScreen : Routes()
//
//
//    val TrapBotGameScreenRoute = "trap_bot_game?difficulty={difficulty}"

    @Serializable
    data object SavedSudokuResultScreen : Routes()


    @Serializable
    data object MathMemoryMixScreen : Routes()

    @Serializable
    data object LeaderBoardScreen : Routes()

    @Serializable
    data object ProfileScreen : Routes()

    @Serializable
    data class AlgebraGameScreen(val level: Int) : Routes()

    @Serializable
    data object LevelSelection: Routes()

    @Serializable
    data object MathPathScreen : Routes()

    @Serializable
    data object MathPathGameScreen : Routes()

    @Serializable
    data object GameCategoryScreen : Routes()

    @Serializable
    data object PileConnectScreen : Routes()
}

