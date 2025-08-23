package com.google.codelab.gamingzone.presentation.level_based

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LevelBasedScreen(
    maxUnlockedLevel: Int,
    onLevelClick: (Int) -> Unit
) {
    val totalLevels = Int.MAX_VALUE // infinite levels (technically limited by Int)
    val levelList = remember { (1..1000).toList() } // Load chunks as needed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF87CEEB), Color(0xFFB3E5FC)) // sky theme 🌤️
                )
            )
            .padding(12.dp)
    ) {

        Text(
            text = "🎮 Select Level",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF37474F)
            ),
            modifier = Modifier.padding(8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(levelList.size) { index ->
                val levelNumber = levelList[index]
                val isUnlocked = levelNumber <= maxUnlockedLevel
                val justUnlocked = levelNumber == maxUnlockedLevel
                val isReward = levelNumber % 10 == 0
                //    Log.e("Level is Unlocked",isUnlocked.toString() + "max-${maxUnlockedLevel}"+ "level=${levelNumber}")

                LevelItems(
                    level = levelNumber,
                    isUnlocked = isUnlocked,
                    onClick = { if (isUnlocked) onLevelClick(levelNumber) },
                    isReward = isReward,
                )
            }
        }
    }
}

@Composable
fun infiniteTransitionGlowing(shouldGlow: Boolean): State<Float> {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (shouldGlow) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
}


@Composable
fun LevelItems(level: Int, isUnlocked: Boolean, isReward: Boolean,onClick: () -> Unit) {
    val scale = remember { Animatable(1f) }

    // Bounce animation for unlocked
    LaunchedEffect(isUnlocked) {
        if (isUnlocked) {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
            scale.animateTo(1f, tween(200))
        }
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .scale(scale.value)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isUnlocked) Color.White else Color(0xFFB0BEC5))
            .border(3.dp, Color.Black, RoundedCornerShape(16.dp))
            .clickable(enabled = isUnlocked) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {
            isReward -> {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Email, // badge 🏆
                    contentDescription = "Reward",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(40.dp)
                )
            }
            isUnlocked -> {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Star, // star ⭐ for unlocked
                    contentDescription = "Unlocked Level",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(40.dp)
                )
            }
            else -> {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Lock, // lock 🔒
                    contentDescription = "Locked",
                    tint = Color(0xFF546E7A),
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Level Number (small text under icon)
        Text(
            text = "$level",
            style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp),
            color = if (isUnlocked) Color.Black else Color.DarkGray
        )
    }
}



