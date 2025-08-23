package com.google.codelab.gamingzone.presentation.profile_stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity

@Composable
fun ProfileScreen(
    statsViewModel: StatsViewModel = hiltViewModel(),
    profile: OverallProfileEntity,
    perGameStats: List<PerGameStatsEntity> = emptyList(),
    onChangeAvatar: () -> Unit,
    onChangeUsername: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF6A8BFF), Color(0xFFB46CFF))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f))
                .clickable { onChangeAvatar() },
            contentAlignment = Alignment.Center
        ) {
            if (profile.avatarUri.toString().isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(profile.avatarUri),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            } else {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Username
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = profile.username,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = onChangeUsername) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit username",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // XP & Coins chips
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatChip(icon = "⭐", label = "XP", value = profile.totalXP.toString())
            StatChip(icon = "💰", label = "Coins", value = profile.coins.toString())
        }

        Spacer(Modifier.height(20.dp))

        // 🔥 NEW: Level carousel with XP progress
        LevelCarousel(profile = profile)

        Spacer(Modifier.height(20.dp))

        // Overall stats
        SectionCard(title = "Overall Stats") {
            StatsRow("Total Games", profile.totalGamesPlayed)
            StatsRow("Wins 🏆", profile.totalWins)
            StatsRow("Losses ❌", profile.totalLosses)
            StatsRow("Best Streak 🔥", profile.bestStreak)
            StatsRow("Highest Level 📈", profile.overallHighestLevel)
            StatsRow("Hints 💡", profile.totalHintsUsed)
            StatsRow("Time ⏱", "${profile.totalTimeSeconds / 60} min")
        }

        Spacer(Modifier.height(16.dp))

        // Per-Game stats
        if (perGameStats.isNotEmpty()) {
            SectionCard(title = "Per-Game Stats") {
                perGameStats.forEach { gameStats ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        tonalElevation = 4.dp,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                gameStats.gameName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color(0xFF2C1A4A),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Level: ${gameStats.highestLevel}")
                                Text("XP: ${gameStats.xp}")
                                Text("Wins: ${gameStats.wins}")
                            }
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Streak: ${gameStats.bestStreak}")
                                Text("Hints: ${gameStats.totalHintsUsed}")
                                Text("Time: ${gameStats.totalTimeSeconds / 60} min")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatChip(icon: String, label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.2f),
        tonalElevation = 4.dp,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(Modifier.width(4.dp))
            Text("$label: $value", fontSize = 14.sp, color = Color.White)
        }
    }
}

@Composable
fun LevelCarousel(profile: OverallProfileEntity) {
    val currentLevel = profile.overallHighestLevel
    val currentXP = profile.totalXP
    val xpForNextLevel = (currentLevel) * 500  // example XP rule
    val progress = currentXP.toFloat() / xpForNextLevel.toFloat()
    val xpNeeded = xpForNextLevel - currentXP

    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.95f),
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .width(250.dp)
                    .height(140.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("LEVEL $currentLevel", fontWeight = FontWeight.Bold, color = Color(0xFF2C1A4A))
                    Spacer(Modifier.height(8.dp))
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = progress.coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = Color(0xFF6A8BFF)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Earn $xpNeeded XP to reach Level ${currentLevel + 1}", fontSize = 14.sp, color = Color(0xFF5B4D7B))
                }
            }
        }
    }
}


@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.9f),
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38225E)
                )
            )
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun StatsRow(label: String, value: Any) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF5B4D7B))
        Text(value.toString(), fontWeight = FontWeight.Bold, color = Color(0xFF2C1A4A))
    }
}
