package com.google.codelab.gamingzone.presentation.game_detail_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import com.google.codelab.gamingzone.presentation.games.math_path.DifficultyMath
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.Difficulty
import com.google.codelab.gamingzone.presentation.home_screen.components.GameDetailScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    id: String? = null,
    game: GameItem,
    onDifficultyChange: (Difficulty) -> Unit,
    onStartGameClick: (GameItem, GameMode?, Difficulty?) -> Unit,
    onBack: () -> Unit,
    onHistoryClick:(GameItem)-> Unit
) {
    val pageCount = game.tutorialImageUrls?.size
    val pagerState = rememberPagerState(
        initialPage = 0
    )


    val isVisible = remember { mutableStateOf(false) }

    val isDifficultyVisible = remember { mutableStateOf(true) }

        if (game.id == "math") {
            isVisible.value=true
            isDifficultyVisible.value=false
        } else {
            isVisible.value=false
            isDifficultyVisible.value=true
        }

    var selectedGameMode by remember { mutableStateOf<GameMode?>(null) }
    var selectedDifficulty = remember { mutableStateOf<Difficulty?>(null) }

    var showHowToPlay by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            GameDetailScreenTopAppBar(
                onBackClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Black),
                title = game.name.toString(),
                onSearchClick = {
                    onHistoryClick(game)
                }
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
                                contentScale = ContentScale.FillBounds,
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
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(100.dp)
                                .width(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Select Game Mode", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(10.dp))

                if (game.id == "math_memory") {
                    OutlinedButton(
                        onClick = { showHowToPlay = true},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Show How to Play")
                    }
                }

                if (showHowToPlay) {
                    HowToPlayOverlay(
                        onDismiss = { showHowToPlay = false }
                    )
                }

                AnimatedVisibility(
                    visible = isVisible.value
                ) {
                    Column {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            GameMode.values().forEach { mode ->
                                OutlinedButton(
                                    onClick = {
                                        selectedGameMode = mode
                                        if (mode == GameMode.LEVEL) selectedDifficulty.value = null
                                    },
                                    border = BorderStroke(
                                        2.dp,
                                        if (selectedGameMode == mode) Color.Black else Color.LightGray
                                    ),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
                                }
                            }
                        }


                        if (selectedGameMode == GameMode.TIMED) {

                            Column {
                                Spacer(Modifier.height(12.dp))

                                Text("Select Difficulty", style = MaterialTheme.typography.titleMedium)

                                Spacer(Modifier.height(12.dp))

                                Row(horizontalArrangement = Arrangement.SpaceEvenly) {

                                    Difficulty.entries.forEach { difficulty ->
                                        val isSelected = difficulty == selectedDifficulty.value
                                        OutlinedButton(
                                            onClick = {
                                                selectedDifficulty.value = difficulty
                                                onDifficultyChange(difficulty)
                                            },
                                            border = BorderStroke(
                                                2.dp,
                                                if (selectedDifficulty.value == difficulty) Color.Black else Color.LightGray
                                            ),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (isSelected) Color.Blue.copy(alpha = 0.1f) else Color.Transparent,
                                                contentColor = if (isSelected) Color.Blue else Color.Black
                                            ),
                                            shape = RoundedCornerShape(50),
                                            modifier = Modifier.padding(horizontal = 6.dp)
                                        ) {
                                            Text(difficulty.name)
                                        }
                                    }
                                }
                            }


                        }
                    }

                }






                AnimatedVisibility(visible = isDifficultyVisible.value) {

                    Column {
                        Text(text = "Select Difficulty", fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                            Difficulty.values().forEach { difficulty ->
                                val isSelected = difficulty == selectedDifficulty.value
                                OutlinedButton(
                                    onClick = {
                                        selectedDifficulty.value = difficulty
                                        onDifficultyChange(difficulty)
                                    },
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
                    }


                }


                OutlinedButton(
                    enabled = selectedDifficulty.value != null || selectedGameMode == GameMode.LEVEL || game.id == "color",
                    onClick = {
                        if (game.id == "color") {
                            onStartGameClick(game, null, null)
                        } else {
                            onStartGameClick(game, selectedGameMode, selectedDifficulty.value)
                        }
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



}

@Composable
fun HowToPlayOverlay(
    onDismiss: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            Modifier.padding(32.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("How to Play", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                Text("1️⃣ Memorize the sequence of operations.", style = MaterialTheme.typography.bodyLarge)
                Text("2️⃣ Start from the given number and apply each operation in order.", style = MaterialTheme.typography.bodyLarge)
                Text("3️⃣ Enter the final result when ready.", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(24.dp))
                Button(onClick = onDismiss) {
                    Text("Got it!")
                }
            }
        }
    }
}



