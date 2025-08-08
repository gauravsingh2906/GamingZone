package com.google.codelab.gamingzone.presentation.profile_screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.codelab.gamingzone.R
import kotlinx.coroutines.delay

//@Composable
//fun ProfileScreen() {
//    val categories = listOf("FUN", "PUZZLE", "ENTERTAINMENT", "SKILL")
//    var selectedCategory by remember { mutableStateOf("FUN") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF0B0F2F)) // Dark fantasy background
//            .padding(16.dp)
//    ) {
//        // Title
//        Text(
//            text = "⭐ ${selectedCategory.uppercase()} 😊",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFFFFC107)
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        // Tab Row
//        LazyRow {
//            items(categories) { category ->
//                val isSelected = category == selectedCategory
//                Text(
//                    text = category,
//                    color = if (isSelected) Color.Yellow else Color.White,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .padding(horizontal = 12.dp)
//                        .clickable { selectedCategory = category }
//                )
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // Game Grid
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(8.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            items(getGamesForCategory(selectedCategory)) { game ->
//                GameCardItem(game)
//            }
//        }
//    }
//}
//
//data class GameItem(
//    val title: String,
//    val description: String,
//    val imageRes: Int
//)
//
//
//
//@Composable
//fun GameCardItem(game: GameItem) {
//    Card(
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2240)),
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        Column(
//            modifier = Modifier.padding(12.dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = game.imageRes),
//                    contentDescription = game.title,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(12.dp)),
//                    contentScale = ContentScale.Crop
//                )
//
//                Icon(
//                    imageVector = Icons.Default.Favorite,
//                    contentDescription = "Favorite",
//                    tint = Color.Red,
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(4.dp)
//                )
//            }
//
//            Text(
//                text = game.title,
//                fontWeight = FontWeight.Bold,
//                fontSize = 14.sp,
//                color = Color.White,
//                modifier = Modifier.padding(top = 6.dp)
//            )
//            Text(
//                text = game.description,
//                fontSize = 12.sp,
//                color = Color.LightGray
//            )
//        }
//    }
//}
//
//fun getGamesForCategory(category: String): List<GameItem> {
//    return listOf(
//        GameItem("Candy Quest", "Match three puzzle game", R.drawable.star),
//        GameItem("Happy Jump", "Endless jumping action", R.drawable.star),
//        GameItem("Emoji Search", "Emoji matching fun", R.drawable.star),
//        GameItem("Ball Bounce", "Physics based challenge", R.drawable.star),
//        GameItem("Color Splash", "Creative painting game", R.drawable.star),
//        GameItem("Pet Pals", "Care for cute pets", R.drawable.star),
//        GameItem("Bubble Pop", "Bubble shooting fun", R.drawable.star),
//        GameItem("Memory Match", "Logic & memory", R.drawable.star),
//        GameItem("Tile Tumble", "Logic puzzle game", R.drawable.star)
//    )
//}


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .background(Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E2F))))
            .fillMaxSize()
            .padding(16.dp)
    ) {
     //   item { AutoScrollingBanner() }

        val banners = listOf(
            R.drawable.star, // Replace with real drawable or image URLs
            R.drawable.cartoonhouse,
            R.drawable.market
        )

        item { TopBannerCarousel(bannerList = banners) }
        item { SectionHeader("🎮 Recommended For You") }
        item { RecommendedGamesSection() }

        item { SectionHeader("🕹️ Recently Played") }
      //  item { RecentlyPlayedSection() }

        item { SectionHeader("🏆 Your Stats & Achievements") }
        item { SkillStatsCard() }

        item { SectionHeader("🆕 New & Coming Soon") }
        item { NewGamesSection() }

        item { SectionHeader("🎯 Daily Missions") }
        item { DailyMissions() }

        item { SectionHeader("🏁 Friend Leaderboard") }
      //  item { FriendLeaderboard() }

        item { SectionHeader("🔥 Weekly Challenge") }
       // item { WeeklyChallenge() }

        item { SectionHeader("📊 Feedback Poll") }
        item { FeedbackPoll() }

        item { PersonalTrackerSection() }
    }
}

@Composable
fun PersonalTrackerSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B3D))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📈 Your Top Played: Fun 🎉", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Avg. session: 5m | Best Score: 840 in Tile Tumble", color = Color.Gray, fontSize = 12.sp)
        }
    }
}


@Composable
fun SkillStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C3A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎯 You’re great at Puzzle Games!", color = Color.White, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge("Puzzle Pro")
                Badge("Casual King")
            }
        }
    }
}

@Composable
fun NewGamesSection() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(2) {
            GameCard("Jungle Rush", "New")
        }
        items(2) {
            ComingSoonCard("Emoji World")
        }
    }
}

@Composable
fun ComingSoonCard(title: String) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray.copy(alpha = 0.3f))
            .blur(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Coming Soon\n$title", textAlign = TextAlign.Center, color = Color.White)
    }
}

@Composable
fun FeedbackPoll() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF37474F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🗳️ Did you enjoy this game?", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                listOf("😍", "🙂", "😐", "🙁").forEach {
                    Text(text = it, fontSize = 28.sp, modifier = Modifier.clickable { /* handle vote */ })
                }
            }
        }
    }
}



@Composable
fun Badge(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF4CAF50), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, fontSize = 12.sp)
    }
}


@Composable
fun DailyMissions() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF263238))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎯 Complete 2 Puzzle Games to earn 50 stars!",
                color = Color.White, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Color(0xFFFF7043),
                trackColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("1/2 completed", color = Color.Gray, fontSize = 12.sp)
        }
    }
}


@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun AutoScrollingBanner() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(3) { index ->
            Card(
                modifier = Modifier
                    .size(280.dp, 160.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "🎉 Game of the Week!",
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    )
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF7043),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Play Now")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBannerCarousel(bannerList: List<Int>) {


    val pagerState = rememberPagerState(initialPage = 0) {
        bannerList.size
    }

    var isTouched = remember { mutableStateOf(false) }

    LaunchedEffect(isTouched.value) {
        while (!isTouched.value) {
            delay(4000L)
            val nextPage = (pagerState.currentPage + 1) % bannerList.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    // Auto-scroll effect


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 16.dp,
            beyondViewportPageCount = 1,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isTouched.value = true // Pause on press
                            tryAwaitRelease() // Wait until user lifts finger
                            isTouched.value = false // Resume auto-scroll
                        }
                    )
                }
        ) { index ->
            val imageRes = bannerList[index]
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Banner $index",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        // Page indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(bannerList.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 10.dp else 6.dp)
                        .background(
                            if (isSelected) Color.White else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}


@Composable
fun RecommendedGamesSection() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(5) { index ->
            GameCard(title = "Emoji Puzzle", category = "Brainy")
        }
    }
}

@Composable
fun GameCard(title: String, category: String) {
    Card(
        modifier = Modifier.size(width = 140.dp, height = 180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C3A))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(category, color = Color.Gray, fontSize = 12.sp)
        }
    }
}







