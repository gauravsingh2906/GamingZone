package com.google.codelab.gamingzone.presentation.navigation

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.codelab.gamingzone.domain.model.GameCategory
import com.google.codelab.gamingzone.presentation.games.chess_screen3.ChessScreen
import com.google.codelab.gamingzone.presentation.games.chess_screen3.ChessViewModel
import com.google.codelab.gamingzone.presentation.games.color_race_screen2.ColorRaceEvent
import com.google.codelab.gamingzone.presentation.games.color_race_screen2.ColorRaceScreen
import com.google.codelab.gamingzone.presentation.games.color_race_screen2.ColorRaceViewModel
import com.google.codelab.gamingzone.presentation.home_screen.HomeScreen
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.sampleGames
import com.google.codelab.gamingzone.presentation.navigation.Routes.GameDetailScreen
import com.google.codelab.gamingzone.presentation.navigation.Routes.SudokuScreen
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuAction
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuGameEvent
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuViewModel
import com.google.codelab.gamingzone.presentation.home_screen.HomeViewModel
import com.google.codelab.gamingzone.presentation.leaderboard_screen.LeaderboardScreen
import com.google.codelab.gamingzone.presentation.profile_screen.ProfileScreen


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
        startDestination = Routes.HomeScreen
    ) {



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
                    navController.navigate(GameDetailScreen(gameId))
                },
                onStartChallenge = { challenge ->
                    navController.navigate(challenge.gameRoute)
                },
                scrollBehavior = scrollBehavior
            )
        }



        composable<GameDetailScreen> {

            val gameId = it.toRoute<GameDetailScreen>().gameId
            val viewModel: SudokuViewModel = hiltViewModel()
            val state = viewModel.state

            val game = viewModel.getGameById(gameId)

            com.google.codelab.gamingzone.presentation.game_detail_screen.GameDetailScreen(
                id = gameId,
                game = game!!,
                difficultyOptions = Difficulty.entries,
                onDifficultyChange = { newDifficulty ->
                    viewModel.onAction(SudokuAction.ChangeDifficulty(newDifficulty))
                },
                onStartGameClick = { gameItem ->
                    val selectedDifficulty = state.value.difficulty?.name ?:"EASY"
                    val destination = gameItem.buildScreen(selectedDifficulty)
                    navController.navigate(destination)
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<SudokuScreen> {
            val gameId = it.toRoute<SudokuScreen>().game
            val viewModel: SudokuViewModel = hiltViewModel()
            val state = viewModel.state

            val showWinDialog = remember { mutableStateOf(false) }
            val showLoseDialog = remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                viewModel.event.collect { event ->
                    when (event) {
                        is SudokuGameEvent.PuzzleSolved -> {
                            showWinDialog.value = true
                        }

                        is SudokuGameEvent.GameOver -> {
                            showLoseDialog.value = true
                        }
                    }
                }
            }



            com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuScreen(
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
        }

        composable<Routes.ColorRaceScreen> {
            val viewModel: ColorRaceViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value

            // Get difficulty from route
            val difficulty = it.toRoute<Routes.ColorRaceScreen>().difficulty

//            LaunchedEffect(difficulty) {
//                viewModel.onEvent(ColorRaceEvent.Restart(difficulty))
//            }

            val showWinDialog = remember { mutableStateOf(false) }
            val showLoseDialog = remember { mutableStateOf(false) }

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
                onEvent = { event ->
                    viewModel.onEvent(event)

                    // Navigate after game over and dialog dismiss
                    if (event is ColorRaceEvent.DismissGameOverDialog &&
                        !state.isShowingSequence && state.isGameOver
                    ) {

                    }
                }
            )
        }

        composable<Routes.ChessScreen> {
            val gameId = it.toRoute<Routes.ChessScreen>().game
            val viewModel: ChessViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value

            ChessScreen(

            )
        }

        composable<Routes.LeaderBoardScreen> {
            LeaderboardScreen()
        }
        composable<Routes.ProfileScreen> {
            ProfileScreen(

            )
        }


    }
}


