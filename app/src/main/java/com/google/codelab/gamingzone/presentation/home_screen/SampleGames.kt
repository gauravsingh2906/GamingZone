package com.google.codelab.gamingzone.presentation.home_screen

import androidx.compose.foundation.lazy.LazyColumn
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.domain.model.GameCategory
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.navigation.Routes

object SampleGames {

   val sampleGames = listOf(
        GameItem(
            id = "sudoku",
            name = "Sudoku",
            category = GameCategory.LOGIC,
            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
            coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
            tutorialImageUrls = listOf(
                "https://example.com/sudoku_tut1.jpg",
                "https://example.com/sudoku_tut2.jpg",
                "https://example.com/sudoku_tut3.jpg",
                "https://example.com/sudoku_tut4.jpg"
            ),
            difficultyLevels = listOf("Easy", "Medium", "Hard"),
            gameImageUrls = listOf(),
            buildScreen = { difficulty->
                Routes.SudokuScreen(
                    game = "Sudoku",
                )
            }
        ),GameItem(
           id = "ColorRaceScreen",
           name = "ColorRace",
           category = GameCategory.SKILL,
           description = "Color Race Screen",
           coverImageUrl = "https://lh3.googleusercontent.com/fife/ALs6j_HKrh126EIs0xX-nLS0s9LoK_zlZ6LwJ32IX-tSL8Dypn6nSKI5eUkKKkr5WDQr_X-4HYta8oYH0o8cozRhxA8W_naS2v_LmGaVFdqOszYpna3lGE-HOYmIAXx5qQ0icmGPrLTOaNr591N3kTmkmniJm354G8T6Jc-EslF6ZPSwBV2xEYwOBZZitWi5uUBGCyq5sw5nbDLK-SelBQgfYg-Zt2mNRd6nt50jeJKQpJWI8cvXi488zWxF64V3V-g-yF7znpjyGhoCODhm4Mck4rP5OK8mj0EHB14FKdZZFIO9KdoDr0vPGbeQbNG5n4HQLclV7khQjXdU1GPiYusZFb-Z2izjpxOXQeR5wU5JC0zVdTfnxDH4QpRIhM2y1Vk7daI72febPRS1ELIWLKQWHulI9FxUtoLaZmpyuLh25kN_MocFuyfc7Reviz3Ij-oa55rY4NK0XR95-tdjKxU63xG_Ch3Wy5QH7v8xvMkTwLGXwdJi1kfeZGNhAVNobc8cGiv-kArjkvKtNugXoXyy2bJmiklaMMkg_yv8KiZ_GzQ4yPe5PrMWJz6C3zBPC1Wa0YCIRvJkrzTRxNyXPbU65mzwRDEmgU--xhJX8nK0QvrGydqeASa7lL2pmQj0-FtonkL961s4LCrafeQlalR87QAL-EaMoFLtdt_yEeDc-bhZsHrgQkRAgWBrlUq6NgmfFm9UZNVSsEz_RuyCmDVUUXg4NJQURFImg7Kc-MQL8ai1kTBWNe97AU342cK8NZOIxORC7Ua4zmTHjuFCqYjWpB1eWYqFRt_gRcqtXi8gsdYUdoFcAJtUpD4JfHGNyWMQTWGDbA5LsSHvO9jq3yz5NxQI0BwysenNdcL1-cep0N2beWmHqok9ss7eyidjWp75Cz51AfJ9U8pQTnX0AOuVouGrnDnD7021fvPx5VrVh9yGmyHQcumbfcCv-Y25Q10-BEYdT53ds0Mnp-EMLIIIlAieaoWDT8Avm1DFRMywypC34yQjrbb2YbZ2uj9ckeP_05n1zNwUD5hGKnY4Ekc-sQ0hcxy2ymp-1tToz4hsXEdLC5JkGYp299gtcTCgg7KTsPcXr6RMLkbDcnEg7IR3UDT6GqITkpaC1dPMqCAR4fQ3FxQRdli9wRSdklCR711CeSqAj6rbZLG73yDBLL6iJ__sgdMs-12peHm3g5gAd1Xc0u767hnKPrN0lVD-3SXaXOOHehslzZRNEbCBAR6cuqgv3qbzMB19vlO6ena3UWKMY6_U8Ih0ekxzFtanj0IYW0RNgjVvtLBdlTFljUcESpbRMXRH18wQ62OcvdYeb-hRiyhqbfuVEozOUhK7DEeacKt7NteHBAcFc7HwcWHkZ35EXFyoRBzDAVV5AseoF8pcdrPN2pPMqCOVO8ywzQNwcgOqH2MuLS2V5KnDHZ5rUA=s1024",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen = { difficulty->
               Routes.ColorRaceScreen(
                   gameId = "ColorRaceScreen",
                   difficulty = difficulty
               )
           }
       ),
       GameItem(
           id = "chess_game",
           name = "Chess",
           category = GameCategory.SKILL,
           description = "Play the classic chess against AI or a friend.",
           coverImageUrl = "https://plus.unsplash.com/premium_photo-1672855191351-e26398f27e5f?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8Y2hlc3N8ZW58MHx8MHx8fDA%3D",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.ChessScreen(
                   game = "Chess",
                   difficulty = difficulty
               )
           }
       ),
       GameItem(
           id = "2",
           name = "Strategy Legends",
           category = GameCategory.REFLEX,
           description = "Strategize and win!",
           coverImageUrl = "https://cdn.pixabay.com/photo/2017/01/10/15/36/sudoku-1963928_960_720.jpg",
           gameImageUrls = listOf("https://example.com/game2.jpg"),
           buildScreen = {
               Routes.GameDetailScreen(it)
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",
               )
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku"
               )
           }
       ), GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku"
               )
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",
               )
           }
       ),GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",
               )
           }
       ),GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",
               )
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",

               )
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",

               )
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",

               )
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",

               )
           }
       ),
       GameItem(
           id = "sudoku",
           name = "Sudoku",
           category = GameCategory.SKILL,
           description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
           coverImageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-sudoku-game-design_23-2149288592.jpg?t=st=1745937947~exp=1745941547~hmac=d3d07fa8706b09458a5c08027449e19df8d08e49977fdc043d19985fdba47958&w=1380",
           tutorialImageUrls = listOf(
               "https://example.com/sudoku_tut1.jpg",
               "https://example.com/sudoku_tut2.jpg",
               "https://example.com/sudoku_tut3.jpg",
               "https://example.com/sudoku_tut4.jpg"
           ),
           difficultyLevels = listOf("Easy", "Medium", "Hard"),
           gameImageUrls = listOf(),
           buildScreen ={ difficulty->
               Routes.SudokuScreen(
                   game = "Sudoku",

               )
           }
       ),

    )

    fun generateDailyChallenge(): DailyChallenge {
        val challenges = listOf(
            DailyChallenge(
                title = "Fast Sudoku Master!",
                description = "Solve a Sudoku in less than 3 minutes!",
                gameRoute = Routes.SudokuScreen(game = "Sudoku"),
                timeLimitSeconds = 180
            ),
            DailyChallenge(
                title = "Color Memory Pro",
                description = "Complete 12 color sequences without mistake!",
                gameRoute = Routes.ColorRaceScreen(gameId = "ColorRace", difficulty = "Hard")
            )
        )
        return challenges.random()
    }

}