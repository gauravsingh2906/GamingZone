package com.google.codelab.gamingzone.presentation.home_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import com.google.codelab.gamingzone.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.domain.model.GameCategory
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.generateDailyChallenge
import com.google.codelab.gamingzone.presentation.home_screen.components.GamingZoneTopAppBar


import androidx.compose.animation.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    games: List<GameItem>,
    scrollBehavior: TopAppBarScrollBehavior,
    selectedCategory: GameCategory?,
    onCategorySelected: (GameCategory?) -> Unit,
    onGameClick: (String) -> Unit,
    onStartChallenge: (DailyChallenge) -> Unit
) {
    var showImagePreview = remember { mutableStateOf(false) }
    var activeImage = remember { mutableStateOf<GameItem?>(null) }
    val dailyChallenge = remember { generateDailyChallenge() }



    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.home_background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        )
        {


            GamingZoneTopAppBar(
                scrollBehaviour = scrollBehavior,
                onSearchClick = { }
            )
            GameCategoryFilter(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                scrollBehaviour = scrollBehavior,
            )


//            DailyChallengeCardAnimated(
//                challenge = dailyChallenge,
//                onStartClick = onStartChallenge
//            )

            val filteredGames = selectedCategory?.let {
                games.filter { it.category == selectedCategory }
            } ?: games

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(filteredGames) { game ->
                    GameCard(
                        game = game,
                        onGameClick = { game.id?.let(onGameClick) },
                        onImageDragStart = { imag ->
                            activeImage.value = imag
                            showImagePreview.value = true
                        },
                        onImageDragEnd = {
                            showImagePreview.value = false
                        },
                    )
                }
            }
        }
    }
}


@Composable
fun GameCard(
    game: GameItem,
    onGameClick: () -> Unit,
    onImageDragStart: (GameItem) -> Unit,
    onImageDragEnd: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                onGameClick()
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { onImageDragStart(game) },
                    onDragCancel = { onImageDragEnd() },
                    onDragEnd = { onImageDragEnd() },
                    onDrag = { _, _ -> }
                )
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {

        Column() {
            AsyncImage(
                model = game.coverImageUrl,
                contentDescription = game.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = game.name.toString(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = game.category?.name?.lowercase()!!.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCategoryFilter(
    selectedCategory: GameCategory?,
    onCategorySelected: (GameCategory?) -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    val categories = listOf(null) + GameCategory.entries.toTypedArray()
    LazyRow {
        items(categories) { category ->
            val isSelected = category == selectedCategory

            val label = category?.name ?: "All"
            FilterChip(
                selected = isSelected,
                onClick = {
                    onCategorySelected(category)
                },
                label = { Text(label, color = Color.Black) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun DailyChallengeCardAnimated(
    challenge: DailyChallenge,
    onStartClick: (DailyChallenge) -> Unit
) {
    val animationSpec = remember { tween<Float>(durationMillis = 800) }
    val offsetY = remember { Animatable(300f) } // Start 300 pixels below
    val alpha = remember { Animatable(0f) } // Start invisible

    LaunchedEffect(Unit) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = animationSpec
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = animationSpec
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .offset(y = offsetY.value.dp)
            .alpha(alpha.value),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Daily Challenge",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = challenge.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { onStartClick(challenge) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Start Challenge")
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun Preview(modifier: Modifier = Modifier) {
//
//    val sampleGames = listOf(
//        GameItem(
//            id = "sudoku",
//            name = "Sudoku",
//            category = GameCategory.LOGIC,
//            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
//            coverImageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/92/Sudoku_Puzzle_%28a_puzzle_with_total_symmetry%29.png",
//            tutorialImageUrls = listOf(
//                "https://cdn.pixabay.com/photo/2015/08/02/17/20/sudoku-872218_960_720.jpg",
//                "https://cdn.pixabay.com/photo/2017/01/10/15/36/sudoku-1963928_960_720.jpg",
//                "https://cdn.pixabay.com/photo/2017/01/10/15/39/sudoku-1963931_960_720.jpg"
//            ),
//            difficultyLevels = listOf("Easy", "Medium", "Hard"),
//            gameImageUrls = listOf(
//                "https://example.com/sudoku_game1.jpg",
//                "https://example.com/sudoku_game2.jpg"
//            ),
//            screenRoute = ""
//        ),GameItem(
//            id = "colorracescreen",
//            name = "ColorRaceScreen",
//            category = GameCategory.SKILL,
//            description = "Use logic to place numbers from 1-9 into each cell of a 9×9 grid.",
//            coverImageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/92/Sudoku_Puzzle_%28a_puzzle_with_total_symmetry%29.png",
//            tutorialImageUrls = listOf(
//                "https://example.com/sudoku_tut1.jpg",
//                "https://example.com/sudoku_tut2.jpg",
//                "https://example.com/sudoku_tut3.jpg",
//                "https://example.com/sudoku_tut4.jpg"
//            ),
//            difficultyLevels = listOf("Easy", "Medium", "Hard"),
//            gameImageUrls = listOf(
//                "https://example.com/sudoku_game1.jpg",
//                "https://example.com/sudoku_game2.jpg"
//            ),
//            screenRoute = ""
//        )
//    )
//
//    HomeScreen(
//        games = sampleGames,
//        selectedCategory = null,
//        onCategorySelected = {},
//        onGameClick = {},
//        )
//
//}


