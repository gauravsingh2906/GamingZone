package com.google.codelab.gamingzone.presentation.home_screen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.codelab.gamingzone.R
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.domain.model.GameCategory
import com.google.codelab.gamingzone.presentation.games.math_memory.CardStyle
import com.google.codelab.gamingzone.presentation.games.math_memory.GameTheme
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.navigation.Routes

object SampleGames {

    val sampleGames = listOf(
        GameItem(
            id = "sudoku",
            name = "Sudoku",
            category = GameCategory.LOGIC,
            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
            coverImageUrl = R.drawable.freepik_chess,
            tutorialImageUrls = listOf(
                R.drawable.freepik_chess,R.drawable.chess_freepik0,R.drawable.chess_freepik1,R.drawable.freepik_chess
            ),
            difficultyLevels = listOf("Easy", "Medium", "Hard"),
            gameImageUrls = listOf(),
            //         buildScreen = { difficulty->
//                Routes.SudokuScreen(
//                    game = "Sudoku",
//                    difficulty = Difficulty.EASY.toString()
//                )
//            }
        ),
        GameItem(
            id = "chess",
            name = "Chess",
            category = GameCategory.LOGIC,
            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
            coverImageUrl = R.drawable.chess_home,
            tutorialImageUrls = listOf(
                R.drawable.chesser,R.drawable.chess_freepik0,R.drawable.chess_freepik1,R.drawable.chess_home
            ),
            difficultyLevels = listOf("Easy", "Medium", "Hard"),
            gameImageUrls = listOf(),
//           buildScreen = { difficulty->
//               Routes.ChessScreen(
//                   game = "Chess",
//                   difficulty = Difficulty.EASY.toString()
//               )
//           }
        ),
        GameItem(
            id = "math_memory",
            name = "Math Memory Mix",
            category = GameCategory.PUZZLE,
            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
            coverImageUrl = R.drawable.math_memory_mix,
            tutorialImageUrls = listOf(
                R.drawable.freepik_chess,R.drawable.chess_freepik0,R.drawable.chess_freepik1,R.drawable.chess_freepik0
            ),
            difficultyLevels = listOf("Easy", "Medium", "Hard"),
            gameImageUrls = listOf(),
//           buildScreen = { difficulty->
//               Routes.TrapBotGameScreen(
//                   difficulty =
//               )
//           }
        ),
        GameItem(
            id = "trap",
            name = "TrapTheBot",
            category = GameCategory.LOGIC,
            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
            coverImageUrl = R.drawable.trap_bot,
            tutorialImageUrls = listOf(
                R.drawable.freepik_chess,R.drawable.chess_freepik0,R.drawable.chess_freepik1,R.drawable.chess_freepik1
            ),
            difficultyLevels = listOf("Easy", "Medium", "Hard"),
            gameImageUrls = listOf(),
//           buildScreen = { difficulty->
//               Routes.TrapBotGameScreen(
//                   difficulty =
//               )
//           }
        ),
        GameItem(
            id = "math",
            name = "Math Path",
            category = GameCategory.LOGIC,
            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
            coverImageUrl = R.drawable.mathist,
            tutorialImageUrls = listOf(
                R.drawable.freepik_chess,R.drawable.chess_freepik0,R.drawable.chess_freepik1,R.drawable.chess_freepik1
            ),
            difficultyLevels = listOf("Easy", "Medium", "Hard"),
            gameImageUrls = listOf(),
//           buildScreen = { difficulty->
//               Routes.TrapBotGameScreen(
//                   difficulty =
//               )
//           }
        ),
        GameItem(
            id = "color",
            name = "Color Race",
            category = GameCategory.PUZZLE,
            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
            coverImageUrl = R.drawable.star,
            tutorialImageUrls = listOf(
                R.drawable.freepik_chess,R.drawable.chess_freepik0,R.drawable.chess_freepik1,R.drawable.chess_freepik0
            ),
            difficultyLevels = listOf("Easy", "Medium", "Hard"),
            gameImageUrls = listOf(),
//           buildScreen = { difficulty->
//               Routes.TrapBotGameScreen(
//                   difficulty =
//               )
//           }
        )
    )


//    fun generateDailyChallenge(): DailyChallenge {
//        val challenges = listOf(
//            DailyChallenge(
//                title = "Fast Sudoku Master!",
//                description = "Solve a Sudoku in less than 3 minutes!",
//                gameRoute = `Routes().SudokuScreen(game = "Sudoku", difficulty = Difficulty.EASY.toString()),
//                timeLimitSeconds = 180
//            ),
//            DailyChallenge(
//                title = "Color Memory Pro",
//                description = "Complete 12 color sequences without mistake!",
//                gameRoute = `Routes()`.ColorRaceScreen(gameId = "ColorRace", difficulty = "Hard")
//            )
//        )
//        return challenges.random()
//    }

//    val builtInThemes = listOf(
//        GameTheme(
//            name = "Classic",
//            background = Brush.verticalGradient(listOf(Color(0xFF1B1B1B), Color(0xFF313131))),
//            cardStyle = CardStyle(Color.White, RoundedCornerShape(12.dp), Color.Black),
//            buttonColor = Color(0xFF1976D2),
//            accentColor = Color(0xFF7C4DFF)
//        ),
//        GameTheme(
//            name = "Sunrise",
//            background = Brush.verticalGradient(listOf(Color(0xFFFFE082), Color(0xFFF44336))),
//            cardStyle = CardStyle(Color(0xFFFFF176), RoundedCornerShape(24.dp), Color(0xFFF44336)),
//            buttonColor = Color(0xFFF57C00),
//            accentColor = Color(0xFFFFC400)
//        ),
//        GameTheme(
//            name = "Ocean",
//            background = Brush.verticalGradient(listOf(Color(0xFF4FC3F7), Color(0xFF01579B))),
//            cardStyle = CardStyle(Color(0xFFB3E5FC), RoundedCornerShape(12.dp), Color(0xFF01579B)),
//            buttonColor = Color(0xFF0288D1),
//            accentColor = Color(0xFF80D8FF)
//        )
//        // Add more for unlockables!
//    )

    val Default = listOf(
        GameTheme(
            name = "Classic",
            backgroundImage = R.drawable.theme1,
            textColor = Color.Black,
            buttonColor = Color(0xFFE0E0E0),
            buttonTextColor = Color.Black
        ),
        GameTheme(
            name = "Nature",
            backgroundImage = R.drawable.theme2,
            textColor = Color.Black,
            buttonColor = Color(0xFF388E3C),
            buttonTextColor = Color.White
        ),
        GameTheme(
            name = "Galaxy",
            backgroundImage = R.drawable.theme3,
            textColor = Color.White,
            buttonColor = Color(0xFF512DA8),
            buttonTextColor = Color.White
        ),
        GameTheme(
            name = "Cyber",
            backgroundImage = R.drawable.theme1,
            textColor = Color(0xFF00E5FF),
            buttonColor = Color.Black,
            buttonTextColor = Color(0xFF00E5FF)
        ),
        GameTheme(
            name = "Paper",
            backgroundImage = R.drawable.theme2,
            textColor = Color.DarkGray,
            buttonColor = Color.White,
            buttonTextColor = Color.Black
        )
    )




}