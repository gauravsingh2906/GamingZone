package com.google.codelab.gamingzone.presentation.game_detail_screen

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.home_screen.components.GameDetailScreenTopAppBar
import com.google.codelab.gamingzone.presentation.home_screen.components.GamingZoneTopAppBar
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    id: String? = null,
    game: GameItem,
    difficultyOptions: List<Difficulty> = Difficulty.entries,
    onDifficultyChange: (Difficulty) -> Unit,
    onStartGameClick: (GameItem) -> Unit,
    onBack: () -> Unit
) {
    val pageCount = game.tutorialImageUrls?.size
    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    var selectedDifficulty = remember { mutableStateOf<Difficulty?>(null) }




    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        GameDetailScreenTopAppBar(
            onBackClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
                .background(Color.Black),
            title = game.name.toString(),
            onSearchClick = { }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = game.description.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "How to Play",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            val context = LocalContext.current
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()




            game.tutorialImageUrls?.let { tutorialUrls ->
                if (tutorialUrls.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        count = game.tutorialImageUrls.size ?: 0
                    ) { page ->
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(game.tutorialImageUrls?.get(page))
                                .crossfade(true)
                                .build(),
                            imageLoader = imageLoader,
                            contentDescription = "Tutorial Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    )
                }
            }

            Text(
                text = "Game Preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                game.gameImageUrls?.forEach { imgUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imgUrl)
                            .crossfade(true)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = "Game Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(100.dp)
                            .width(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Select Difficulty", fontWeight = FontWeight.SemiBold)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//            selectedDifficulty.forEach { level ->
//                FilterChip(
//                    selected = selectedDifficulty == level,
//                    onClick = { onDifficultyChange(level) },
//                    label = { Text(level.blanks.toString()) }
//                )
//            }
                difficultyOptions.forEach { difficulty ->
                    val isSelected = difficulty == selectedDifficulty.value
                    OutlinedButton(
                        onClick = {
                            selectedDifficulty.value = difficulty
                            onDifficultyChange(difficulty)
                        },
                        modifier = Modifier.padding(top = 10.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) Color.Blue else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) Color.Blue.copy(alpha = 0.1f) else Color.Transparent,
                            contentColor = if (isSelected) Color.Blue else Color.Black
                        )
                    ) {
                        Text(difficulty.name)
                    }
//                Box(
//                    modifier = Modifier
//                        .width(80.dp)
//                        .height(50.dp)
//                        .clickable{
//                            onDifficultyChange(difficulty)
//                        }.padding(10.dp),
//                    contentAlignment = Alignment.Center,
//                ) {
//
////                    Text(
////                        text = difficulty.name,
////                        textAlign = TextAlign.Center,
////                        modifier = Modifier.
////                        fillMaxWidth()
////                            .height(38.dp),
////                        style = MaterialTheme.typography.bodyLarge
////                    )
//                }
                }
            }

            OutlinedButton(
                onClick = {
                    onStartGameClick(game)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "Start ${game.name}",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun Preview(modifier: Modifier = Modifier) {
//    GameDetailScreen(
//        game = GameItem(
//            id = "sudoku",
//            name = "Sudoku",
//            category = null,
//            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
//            coverImageUrl = "https://example.com/sudoku_cover.jpg",
//            tutorialImageUrls = listOf(
//                "https://upload.wikimedia.org/wikipedia/commons/4/4b/Sudoku-by-L2G-20050714.svg", // Basic filled Sudoku
//                "https://upload.wikimedia.org/wikipedia/commons/f/ff/Sudoku_Puzzle_Hard_for_print.svg", // Empty Sudoku grid
//                "https://upload.wikimedia.org/wikipedia/commons/2/2c/Sudoku_row_column_and_region.svg", // Explaining 3x3 and rows-columns
//                "https://upload.wikimedia.org/wikipedia/commons/1/12/Sudoku_solution_block_symmetric.svg", // Strategy & solving
//                "https://upload.wikimedia.org/wikipedia/commons/f/f4/Sudoku_proper.svg" // Proper formatted Sudoku
//            ),
//            gameImageUrls = listOf(
//                "https://upload.wikimedia.org/wikipedia/commons/4/4b/Sudoku-by-L2G-20050714.svg", // Basic filled Sudoku
//                "https://upload.wikimedia.org/wikipedia/commons/f/ff/Sudoku_Puzzle_Hard_for_print.svg", // Empty Sudoku grid
//                "https://upload.wikimedia.org/wikipedia/commons/2/2c/Sudoku_row_column_and_region.svg", // Explaining 3x3 and rows-columns
//                "https://upload.wikimedia.org/wikipedia/commons/1/12/Sudoku_solution_block_symmetric.svg", // Strategy & solving
//                "https://upload.wikimedia.org/wikipedia/commons/f/f4/Sudoku_proper.svg" // Proper formatted Sudoku
//            ),
//            difficultyLevels = listOf(),
//            screenRoute = ,
//        ),
//        difficultyOptions = Difficulty.entries,
//        onDifficultyChange = { difficulty ->
//            val difficulty = when (difficulty) {
//                Difficulty.EASY -> {
//                    Difficulty.EASY
//                }
//
//                Difficulty.MEDIUM -> {
//                    Difficulty.MEDIUM
//                }
//
//                Difficulty.HARD -> {
//                    Difficulty.HARD
//                }
//            }
//        },
//        onStartGameClick = {game->
//
//        },
//        onBack = { }
//    )
//}


