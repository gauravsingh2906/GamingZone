package com.google.codelab.gamingzone.presentation.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.codelab.gamingzone.SplashScreen
import com.google.codelab.gamingzone.presentation.feature_code_runner.CodeRunnerScreen
import com.google.codelab.gamingzone.presentation.game_detail_screen.DifficultySelectionScreen
import com.google.codelab.gamingzone.presentation.game_detail_screen.GameDetailScreen
import com.google.codelab.gamingzone.presentation.game_detail_screen.GameMode
import com.google.codelab.gamingzone.presentation.games.SortColorsGame
import com.google.codelab.gamingzone.presentation.games.chess_screen3.ChessScreen
import com.google.codelab.gamingzone.presentation.games.chess_screen3.ChessViewModel
import com.google.codelab.gamingzone.presentation.games.color_race_screen2.ColorRaceScreen
import com.google.codelab.gamingzone.presentation.games.color_race_screen2.ColorRaceViewModel
import com.google.codelab.gamingzone.presentation.games.fear_zone.FearZoneScreen
import com.google.codelab.gamingzone.presentation.games.logic_puzzle.LogicPuzzleScreen
import com.google.codelab.gamingzone.presentation.games.masyu_puzzle_japan.SymbolLogicGrid
import com.google.codelab.gamingzone.presentation.games.math_memory.MathMemoryMixScreen


import com.google.codelab.gamingzone.presentation.games.math_path.DifficultyMath
import com.google.codelab.gamingzone.presentation.games.math_path.MathPathGameScreen
import com.google.codelab.gamingzone.presentation.games.math_path.MathPathViewModel
import com.google.codelab.gamingzone.presentation.games.math_trap_dungeon.MathTrapDungeonScreen
import com.google.codelab.gamingzone.presentation.games.maze_puzzle.MazeGameScreen
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SavedSudokuResultsScreen
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuGameEvent
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuScreen
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuViewModel
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.formatTime
import com.google.codelab.gamingzone.presentation.games.trap_bot.DifficultyLevel
import com.google.codelab.gamingzone.presentation.games.trap_bot.TrapTheBotScreen
import com.google.codelab.gamingzone.presentation.games.trap_bot.TrapTheBotViewModel
import com.google.codelab.gamingzone.presentation.home_screen.HomeScreen
import com.google.codelab.gamingzone.presentation.home_screen.HomeViewModel
import com.google.codelab.gamingzone.presentation.leaderboard_screen.MindRiseHomeScreen
import com.google.codelab.gamingzone.presentation.navigation.Routes.ColorRaceScreen
import com.google.codelab.gamingzone.presentation.navigation.Routes.ProfileScreen
import com.google.codelab.gamingzone.presentation.profile_screen.ProfileScreen
import com.google.codelab.gamingzone.presentation.profile_screen.ProfileScreener
import com.google.codelab.gamingzone.presentation.profile_screen.UserProfileViewModel
import com.google.codelab.gamingzone.tobe.TubeScreen
import com.google.codelab.gamingzone.tobe.TubeViewModel


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingValues: PaddingValues
) {

    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = Routes.SplashScreen
    ) {

        composable<Routes.SplashScreen> {
            SplashScreen(
                navigateToHomeScreen = {
                    navController.navigate(Routes.HomeScreen)
                }
            )
        }

        composable<Routes.HomeScreen> {

            val viewModel: HomeViewModel = hiltViewModel()
            val games = viewModel.games.value
            val selectedCategory = viewModel.selectedCategory.value


            HomeScreen(
                games = games,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    viewModel.updateCategory(category)
                },
                onGameClick = { gameId ->
                    navController.navigate("$GameDetailScreenRoute/$gameId")
                },
                onStartChallenge = { challenge ->
                    navController.navigate(challenge.gameRoute)
                },
                scrollBehavior = scrollBehavior
            )
        }
        composable(DifficultyScreenRoute) {
            DifficultySelectionScreen(
                onDifficultySelected = { difficulty ->
                    navController.navigate("trap_bot_game?difficulty=${difficulty.name}")
                }
            )
        }

        composable(
            route = "$GameDetailScreenRoute/{gameId}",
            arguments = listOf(
                navArgument("gameId")
                { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable

            val viewModel: SudokuViewModel = hiltViewModel()

            val game = viewModel.getGameById(gameId)


            GameDetailScreen(
                id = gameId,
                game = game!!,
                onDifficultyChange = { difficulty ->
                    //   navController.navigate("trap_bot_game?difficulty=${difficulty.name}")
                },
                onStartGameClick = { gameItem, mode, difficulty ->
//                    when (gameItem.id) {
//                        "sudoku" -> {
//                            val diff = difficulty as? Difficulty
//                            navController.navigate("sudoku_screen?gameId=${gameItem.id}&difficulty=${diff?.name}")
//                        }
//
//                        "math" -> {
//                            val diff = difficulty as? DifficultyMath
//                            val route = when (mode) {
//                                GameMode.LEVEL -> "math_screen?gameId=${gameItem.id}&mode=${mode.name}"
//                                GameMode.TIMED -> "math_screen?gameId=${gameItem.id}&difficulty=${diff?.name}&mode=${mode.name}"
//                                null -> "math_screen?gameId=${gameItem.id}&mode=${mode?.name}"
//                            }
//                            navController.navigate(route)
//                        }
//
//                        else -> { /* handle other games */
//                        }
//                    }
                    if (gameItem.id == "sudoku") {
                        navController.navigate("sudoku_screen?gameId=${gameItem.id}&difficulty=${difficulty?.name}")
                    } else if (gameItem.id == "chess") {
                        navController.navigate("chess_screen?gameId=${gameItem.id}&difficulty=${difficulty?.name})")
                    } else if (gameItem.id == "trap") {
                        navController.navigate("trap_bot_game?difficulty=${difficulty?.name}")
                    } else if (gameItem.id == "math") {

                        when (mode) {
                            GameMode.LEVEL -> navController.navigate("math_screen?gameId =${gameItem.id}&mode=${mode?.name}")
                            GameMode.TIMED -> navController.navigate("math_screen?gameId=${gameItem.id}&difficulty=${difficulty?.name}&mode=${mode?.name}")
                            else -> navController.navigate("math_screen?gameId =${gameItem.id}&mode=${mode?.name}")
                        }
                    } else if (gameItem.id == "color") {
                        navController.navigate(ColorRaceScreen)
                    } else if (gameItem.id =="math_memory") {
                        navController.navigate(Routes.MathMemoryMixScreen)
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
                onHistoryClick = {
                    if (game.id=="sudoku") {
                        navController.navigate(Routes.SavedSudokuResultScreen)
                    }
                }
            )

        }


//        composable<Routes.GameDetailScreen> {
//
//            val gameId = it.toRoute<GameDetailScreen>().gameId
//            val viewModel: SudokuViewModel = hiltViewModel()
//            val state = viewModel.state
//
//            val game = viewModel.getGameById(gameId)
//
//            com.google.codelab.gamingzone.presentation.game_detail_screen.GameDetailScreen(
//                id = gameId,
//                game = game!!,
//                onDifficultyChange = { newDifficulty ->
//                    viewModel.onAction(SudokuAction.ChangeDifficulty(newDifficulty))
//                },
//                onStartGameClick = { gameItem, difficulty ->
////                    val selectedDifficulty = state.value.difficulty?.name ?:"EASY"
//                    //      val destination = gameItem.buildScreen(difficulty!!.name)
//                    //      navController.navigate(destination)
//                    // navController.navigate(SudokuScreen(gameId, difficulty?.name!!))
//                },
//                onBack = { navController.popBackStack() },
//            )
//        }

        composable(
            route = MathScreenRoute,
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                },
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = DifficultyMath.EASY.name
                }
            )
        ) { backStackEntry ->
            val mode =
                GameMode.valueOf(backStackEntry.arguments?.getString("mode") ?: GameMode.LEVEL.name)
            val difficulty = DifficultyMath.valueOf(
                backStackEntry.arguments?.getString("difficulty") ?: Difficulty.EASY.name
            )

            val viewModel: MathPathViewModel = hiltViewModel()
            val state = viewModel.state

            MathPathGameScreen(
                mode = mode,
                difficulty = (if (mode == GameMode.TIMED) difficulty else null),
                state = state.value,
                onAction = viewModel::onAction,
                navigateToGameOverScreen = {
                    navController.navigateUp()
                }
            )


        }

        composable(
            route = SudokuScreenRoute,
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = DifficultyLevel.EASY.name
                }
            )) {
//            val args = it.toRoute<SudokuScreen>()
//
//            val gameId = args.game
            //       val difficulty = args.difficulty

            val difficulty = it.arguments?.getString("difficulty") ?: Difficulty.EASY.name

            val viewModel: SudokuViewModel = hiltViewModel()
            val profileViewModel: UserProfileViewModel = hiltViewModel()
            val state = viewModel.state

            val showWinDialog = remember { mutableStateOf(false) }
            val showLoseDialog = remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                viewModel.event.collect { event ->
                    when (event) {
                        is SudokuGameEvent.PuzzleSolved -> {
                            profileViewModel.updateAfterGame(
                                score = viewModel.xpEarned,
                                minutesPlayed =state.value.elapsedTime
                            )
                            showWinDialog.value = true
                        }

                        is SudokuGameEvent.GameOver -> {
                            profileViewModel.updateAfterGame(
                                score = viewModel.xpEarned,
                                minutesPlayed = state.value.elapsedTime
                            )
                            showLoseDialog.value = true
                        }
                    }
                }
            }


            SudokuScreen(
                state = state.value,
                onAction = viewModel::onAction
            )

            if (showWinDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        showWinDialog.value = false
                    },
                    title = { Text("🎉 You Won!") },
                    text = { Text("Great job! You completed the puzzle.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showWinDialog.value = false
                                navController.navigate(Routes.SavedSudokuResultScreen) // Go back to previous screen /                        }) {
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            if (showLoseDialog.value) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("😢 You Lost!") },
                    text = { Text("You made 3 mistakes. Try again!") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLoseDialog.value = false
                            navController.navigate(Routes.SavedSudokuResultScreen)
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }

        composable<Routes.SavedSudokuResultScreen> {
            SavedSudokuResultsScreen(
                onBackClick = {
                    navController.navigate(Routes.HomeScreen)
                }
            )
        }

        composable<ColorRaceScreen> {
            val viewModel: ColorRaceViewModel = hiltViewModel()
            val profileViewModel: UserProfileViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value

            // Get difficulty from route
            //    val difficulty = it.toRoute<ColorRaceScreen>().difficulty

//            LaunchedEffect(difficulty) {
//                viewModel.onEvent(ColorRaceEvent.Restart(difficulty))
//            }

            val showWinDialog = remember { mutableStateOf(false) }
            val showLoseDialog = remember { mutableStateOf(false) }

            if (showWinDialog.value) {
//                profileViewModel.updateAfterGame(
//                    score = viewModel.xpEarned,
//                    minutesPlayed = state.elapsedTime
//                )
                AlertDialog(
                    onDismissRequest = {
                        showWinDialog.value = false
                    },
                    title = { Text("🎉 You Won!") },
                    text = { Text("Great job! You completed the puzzle.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showWinDialog.value = false
                                navController.popBackStack() // Go back to previous screen /                        }) {
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            if (showLoseDialog.value) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("😢 You Lost!") },
                    text = { Text("You made 3 mistakes. Try again!") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLoseDialog.value = false
                            navController.popBackStack()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            ColorRaceScreen(
                state = state,
                onEvent = viewModel::onEvent

                // Navigate after game over and dialog dismiss
//                    if (event is ColorRaceEvent.DismissGameOverDialog &&
//                        !state.isShowingSequence && state.isGameOver
//                    ) {
//
//                    }

            )
        }

        composable(
            route = ChessScreenRoute,
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = DifficultyLevel.EASY.name
                }
            )
        ) {
            val viewModel: ChessViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value

            ChessScreen()
        }

        /*composable<Routes.ChessScreen> {
            val gameId = it.toRoute<ChessScreen>().game
            val viewModel: ChessViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value

            ChessScreen(

            )
        }*/

        composable<Routes.LeaderBoardScreen> {

            val viewModel = hiltViewModel<TubeViewModel>()

            // val viewModel = hiltViewModel<MathPathViewModel>()
            //     val state = viewModel.state
            // LeaderboardScreen()
            //AquaFlowGameScreen()
            //    GlitchEscapeApp()\
            // MazeGameScreen()
//            MindRiseHomeScreen(
//                onGameClick = { },
//                onShareClick = { },
//                onProfileClick = { },
//                onLeaderboardClick = { }
//            )
            TubeScreen()
        }


        composable<Routes.MathMemoryMixScreen> {

            MathMemoryMixScreen()
        }


        composable<ProfileScreen> {

//            val viewModel = hiltViewModel<ColorRaceViewModel>()
//            val state = viewModel.state.collectAsState()
            //   ProfileScreen()
          //  MazeGameScreen()
        //    CodeRunnerScreen()
          //  FearZoneScreen()

         //   MathTrapDungeonScreen()

          //  SymbolLogicGrid()

         //   LogicPuzzleScreen()

            ProfileScreener()
        //    MathMemoryMixScreen()


        }

//        composable<Routes.TrapBotGameScreen> { backStackEntry ->
//            val difficulty = backStackEntry.arguments?.getSerializable("difficulty") as DifficultyLevel
//            val viewModel: TrapTheBotViewModel = hiltViewModel()
//            val state = viewModel.state.collectAsState()
//
//
//
//            TrapTheBotScreen(
//                state = state.value,
//                onEvent = viewModel::onEvent
//            )
//        }

        // 🔴 TrapTheBot game screen
        composable(
            route = TrapBotGameRoute,
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = DifficultyLevel.EASY.name
                }
            )
        ) { backStackEntry ->
            val difficultyStr = backStackEntry.arguments?.getString("difficulty") ?: "EASY"
            val difficulty = DifficultyLevel.valueOf(difficultyStr)

            val viewModel: TrapTheBotViewModel = hiltViewModel()
            val state = viewModel.state

            LaunchedEffect(Unit) {
                viewModel.setDifficulty(difficulty)
            }

            TrapTheBotScreen(
                state = state.value,
                onEvent = viewModel::onEvent
            )
        }


    }
}


