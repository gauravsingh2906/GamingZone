package com.google.codelab.gamingzone.presentation.home_screen


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.codelab.gamingzone.FontSize
import com.google.codelab.gamingzone.RobotoCondensedFont
import com.google.codelab.gamingzone.domain.model.GameCategory
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.presentation.home_screen.components.GamingZoneTopAppBar

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    games: List<GameItem>,
    onSearchClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    selectedCategory: GameCategory?,
    onCategorySelected: (GameCategory?) -> Unit,
    onGameClick: (String) -> Unit,
    onStartChallenge: (DailyChallenge) -> Unit
) {
    var showImagePreview = remember { mutableStateOf(false) }
    var activeImage = remember { mutableStateOf<GameItem?>(null) }
  //  val dailyChallenge = remember { generateDailyChallenge() }



    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier.fillMaxSize()
        )
        {
            GamingZoneTopAppBar(
                scrollBehaviour = scrollBehavior,
                onSearchClick = {
                    onSearchClick()
                }
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
                    onDrag = { _, _ ->

                    }
                )
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = game.coverImageUrl,
                contentDescription = game.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = game.name.toString(),
                fontSize = FontSize.EXTRA_REGULAR,
                fontFamily = RobotoCondensedFont(),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                fontSize = FontSize.SMALL,
                text = game.category?.name?.lowercase()!!.replaceFirstChar { it.uppercase() },
            )
            Spacer(modifier = Modifier.height(28.dp))
        }

    }

}


val categoryIcons = mapOf(
    null to Icons.Default.AccountCircle,
    GameCategory.SKILL to Icons.Default.AccountBox,
    GameCategory.FUN to Icons.Default.Face,
    GameCategory.PUZZLE to Icons.Default.Star,
    GameCategory.REFLEX to Icons.Default.CheckCircle,
    GameCategory.LOGIC to Icons.Default.Email
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCategoryFilter(
    selectedCategory: GameCategory?,
    onCategorySelected: (GameCategory?) -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    val categories = listOf(null)+GameCategory.values().toList()
    //  val categories = listOf(null) + GameCategory.entries.toTypedArray()
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            val isSelected = category == selectedCategory

            val label = category?.name ?: "All"
            FilterChip(
                selected = selectedCategory == category,
                onClick = {
                    onCategorySelected(if (selectedCategory == category) null else category)
                },
                leadingIcon = {
                    val icon = categoryIcons[category] ?: Icons.Default.AccountBox

                    if (true) {
                        androidx.compose.material3.Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                },
                label = {
                    Text(
                        text = category?.name
                            ?.lowercase()
                            ?.replaceFirstChar { it.uppercase() } ?: "All"
                    )
                },
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




