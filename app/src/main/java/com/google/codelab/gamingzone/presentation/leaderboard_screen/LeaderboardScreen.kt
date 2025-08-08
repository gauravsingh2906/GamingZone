package com.google.codelab.gamingzone.presentation.leaderboard_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.codelab.gamingzone.InvertedSemicircleWithTopPlayers
import com.google.codelab.gamingzone.R
import com.google.codelab.gamingzone.presentation.ui.theme.upperLeaderboard

@Composable
fun LeaderboardScreen() {
    Box(modifier = Modifier
            .fillMaxSize()
            .background(upperLeaderboard)
    ) {
        // Background Circles
     //   BackgroundCircles()

        InvertedSemicircleWithTopPlayers()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

//            ScoreRing(percent = 79)

            TopThreePlayersSection(
                players = listOf(
                    Player("Blitz Suxan", 1300, R.drawable.cartoonfarm),
                    Player("Bran Dall", 1299, R.drawable.cartoonhouse),
                    Player("Cyu Phu", 1295, R.drawable.farm)
                )
            )

            Spacer(modifier = Modifier.height(64.dp))

            Spacer(modifier = Modifier.height(24.dp))

            OtherPlayersList(
                listOf(
                    Player("Lam May", 1290),
                    Player("Tang Ann", 1283),
                    Player("Key Taslim", 1279),
                    Player("Same Mangat", 1264)
                )
            )
        }
    }
}

@Composable
fun OtherPlayersList(players: List<Player>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        players.forEachIndexed { index, player ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFF3B2362), RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${index + 4}. ${player.name}", color = Color.White, fontSize = 14.sp)
                Text("${player.score}", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

data class Player(
    val name: String,
    val score: Int,
    val avatarRes: Int? = null
)


@Composable
fun TopThreePlayersSection(players: List<Player>) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        players.forEachIndexed { index, player ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = player.avatarRes ?: R.drawable.cartoonhouse),
                    contentDescription = player.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
                Text(player.name, color = Color.White, fontSize = 14.sp)
                Text("${player.score}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Text(
                    "${index + 1}",
                    color = Color.Yellow,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreRing(percent: Int) {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = percent / 100f,
            strokeWidth = 8.dp,
            color = Color(0xFFFFC107),
            modifier = Modifier.size(100.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            androidx.compose.material3.Icon(
                Icons.Default.Email,
                contentDescription = "Email",
                tint = Color(0xFFFFC107)
            )
            Text(
                "$percent%",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}


@Composable
fun BackgroundCircles() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
                )
            )
    ) {
        // Top-left purple circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFF3B2362).copy(alpha = 0.6f),
                radius = size.width * 0.6f,
                center = Offset(x = size.width * -0.2f, y = size.height * 0.1f)
            )
        }

        // Bottom-right blurred purple glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFF3B2362).copy(alpha = 0.4f),
                radius = size.width * 0.8f,
                center = Offset(x = size.width * 1.2f, y = size.height * 1.1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    LeaderboardScreen()
}


data class LevelItem(val number: Int, val offsetX: Dp, val offsetY: Dp, val progress: Float)

//@Composable
//fun LevelSelectionScreen(score: Int = 78049) {
//    val levels = listOf(
//        LevelItem(1, 30.dp, 140.dp, 0.6f),
//        LevelItem(2, 150.dp, 120.dp, 0.4f),
//        LevelItem(3, 270.dp, 140.dp, 0.9f),
//        LevelItem(4, 50.dp, 260.dp, 0.8f),
//        LevelItem(5, 160.dp, 250.dp, 0.5f),
//        LevelItem(6, 280.dp, 270.dp, 0.3f),
//        LevelItem(7, 40.dp, 390.dp, 1f),
//        LevelItem(8, 160.dp, 380.dp, 0.6f),
//        LevelItem(9, 280.dp, 400.dp, 1f)
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(Color(0xFF1E2B5C), Color(0xFF4E64A6))
//                )
//            )
//    ) {
//        GameBackgroundDecor()
//
//        // Buttons + UI
//        Box(Modifier.fillMaxSize()) {
//            HomeFloatingButton(Modifier.align(Alignment.TopStart).padding(16.dp))
//            RocketScore(score, Modifier.align(Alignment.TopEnd).padding(16.dp))
//
//            levels.forEach { level ->
//                LevelButton(
//                    level = level.number,
//                    progress = level.progress,
//                    modifier = Modifier.absoluteOffset(level.offsetX, level.offsetY)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun HomeFloatingButton(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .size(56.dp)
//            .background(Color(0xFFFFA726), CircleShape)
//            .clickable { },
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_home),
//            contentDescription = "Home",
//            modifier = Modifier.size(28.dp)
//        )
//    }
//}
//
//@Composable
//fun RocketScore(score: Int, modifier: Modifier = Modifier) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier
//            .background(Color(0xFF21223A), shape = RoundedCornerShape(16.dp))
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//    ) {
//        Text(
//            text = "$score",
//            color = Color.White,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            fontFamily = FontFamily.SansSerif
//        )
//        Spacer(Modifier.width(8.dp))
//        Image(
//            painter = painterResource(id = R.drawable.ic_rocket),
//            contentDescription = "Rocket",
//            modifier = Modifier.size(28.dp)
//        )
//    }
//}
//
//@Composable
//fun LevelButton(level: Int, progress: Float, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .size(90.dp)
//            .background(Color(0xFFB55400), CircleShape)
//            .border(2.dp, Color(0xFFDA7C1A), CircleShape)
//            .clickable { },
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(
//                text = "$level",
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                fontFamily = FontFamily.SansSerif
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Box(
//                modifier = Modifier
//                    .height(8.dp)
//                    .width(40.dp)
//                    .clip(RoundedCornerShape(4.dp))
//                    .background(Color.Gray)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .fillMaxWidth(progress)
//                        .background(Color.Yellow)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun GameBackgroundDecor() {
//    val planets = listOf(
//        R.drawable.planet_1, R.drawable.planet_2,
//        R.drawable.planet_3, R.drawable.rock_1,
//        R.drawable.rock_2, R.drawable.rock_3
//    )
//
//    planets.shuffled().take(6).forEachIndexed { i, resId ->
//        Image(
//            painter = painterResource(id = resId),
//            contentDescription = null,
//            modifier = Modifier
//                .absoluteOffset(x = (i * 50).dp, y = (i * 80).dp)
//                .size((60 + i * 8).dp)
//        )
//    }
//}


val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1E2B5C), Color(0xFF4E64A6))
)

val RockOrange = Color(0xFFFD9138)
val PlanetBlue = Color(0xFF6FC3DF)
val StarYellow = Color(0xFFFFD700)
val LevelNodeBg = Color(0xFFDC6D00)
val ScoreBoxColor = Color(0xFF2B2F53)








