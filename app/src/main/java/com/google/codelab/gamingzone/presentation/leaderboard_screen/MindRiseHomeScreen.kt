package com.google.codelab.gamingzone.presentation.leaderboard_screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.codelab.gamingzone.presentation.profile_screen.GameCard

@Composable
fun MindRiseHomeScreen(
    userName: String = "Gaurav",
    xp: Int = 560,
    streak: Int = 4,
    onGameClick: (String) -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
    ) {
        // Greeting & XP
        Text("Hi $userName 👋", fontSize = 22.sp, color = Color.White)
        Text("Ready to challenge your mind today?", color = Color.LightGray)
        Spacer(Modifier.height(8.dp))
        Row {
            Text("⭐ XP: $xp", color = Color.Yellow, fontSize = 16.sp)
            Spacer(Modifier.width(12.dp))
            Text("🔥 Streak: $streak", color = Color.Red, fontSize = 16.sp)
        }

        Spacer(Modifier.height(24.dp))

        val topGameList = listOf("Trap The Bot", "Chess vs Bot", "Math Path")
        val quickGameList = listOf("Logic Flip", "Tap the Prime", "Equation Catch")


        // 🔥 Top Challenges
        Text("🔥 Top Challenges", color = Color.White, fontSize = 18.sp)
        LazyRow {
            items(topGameList) { game ->
                GameCard(game.toString(), onGameClick)
            }
        }

        Spacer(Modifier.height(24.dp))

        // 🧪 Quick Games
        Text("🧪 Quick Games (1-Tap)", color = Color.White, fontSize = 18.sp)
        LazyRow {
            items(quickGameList) { game ->
                GameCard(game.toString(), onGameClick)
            }
        }

        Spacer(Modifier.height(24.dp))

        // 📅 Daily Missions
        Text("📅 Daily XP Missions", color = Color.White, fontSize = 18.sp)
        MissionList(
            missions = listOf(
                "✅ Play 2 Games",
                "✅ Earn 50 XP",
                "🔓 Share your score"
            )
        )

        Spacer(Modifier.height(24.dp))

        // Share Button
        OutlinedButton(
            onClick = onShareClick,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.Yellow),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("📤 Invite Friends & Earn XP")
        }

        Spacer(Modifier.height(16.dp))


    }


}
@Composable
fun GameCard(title: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .size(140.dp)
            .padding(8.dp)
            .clickable { onClick(title) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Box(Modifier.padding(12.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MissionList(missions: List<String>) {
    Column(modifier = Modifier.padding(start = 8.dp)) {
        missions.forEach { task ->
            Text(text = task, color = Color.LightGray, fontSize = 14.sp)
        }
    }
}