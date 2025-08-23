// ui/gameover_levelcomplete.kt
package com.google.codelab.gamingzone.presentation.games.algebra

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.util.CoilUtils.result
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun LevelCompletedDialog(
    level: Int,
    earnedScore: Int,
    score: Int,
    xpEarned:Int,
    streak:Int,
    bestStreak:Int,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onHome: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // simple count up
    var startCount by remember { mutableStateOf(false) }
    val animatedScore by animateIntAsState(
        targetValue = if (startCount) earnedScore else 0,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "scoreCount"
    )

    // kick start after composition
    LaunchedEffect(Unit) { startCount = true }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF6A8BFF),
                        Color(0xFFB46CFF)
                    )
                )
            )
    ) {
        // confetti layer
        Confetti(modifier = Modifier
            .fillMaxSize()
            .alpha(0.9f))

        Row(
            modifier = Modifier.padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
            Text("XP +${xpEarned}", color = Color.Green, fontSize = 18.sp)
            Text("Streak: ${streak}", color = Color.Yellow, fontSize = 18.sp)
            Text("Best Streak: $bestStreak", color = Color.Cyan, fontSize = 18.sp)
        }

        // card
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            color = Color(0xFFFDF9FF)
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(max = 520.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_media_previous),
                            contentDescription = "Back",
                            tint = Color(0xFF2C1A4A)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(8.dp))


                Text(
                    text = "Level $level Completed!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2C1A4A),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Nice! You’re getting smarter 🎓",
                    fontSize = 16.sp,
                    color = Color(0xFF5B4D7B),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                // cute medal/mascot (replace with your drawable if you have one)
//                Box(
//                    modifier = Modifier
//                        .size(96.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFFEEE7FF)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    // fallback icon
//
//                }
                Text("🏅", fontSize = 42.sp)



                Spacer(Modifier.height(16.dp))

                Text("Score", fontSize = 14.sp, color = Color(0xFF6D5E90))
                Text(
                    "$animatedScore",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF38225E)
                )

                Spacer(Modifier.height(12.dp))

                // tiny progress hint (optional)
                HintPill(text = "Tip: Keep streaks to earn bonus XP!")

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onReplay,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Replay") }

                    Button(
                        onClick = onNextLevel,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Next Level ▶") }
                }

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = onHome) {
                    Text("Home")
                }

            }
        }
    }
}

@Composable
fun GameOverDialog(
    level: Int,
    score: Int,
    bestScore: Int?, // pass null if you don't track yet
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF9D9D), Color(0xFFFFC6A8))
                )
            )
    ) {
        // gentle floating bubbles
        FloatingBubbles(modifier = Modifier
            .fillMaxSize()
            .alpha(0.35f))

        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            color = Color(0xFFFFF6F2)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 520.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Oops… Try Again!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF5A1E1E),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Level $level beat you this time 😿",
                    fontSize = 16.sp,
                    color = Color(0xFF824040),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))
                Text("Score", fontSize = 14.sp, color = Color(0xFF824040))
                Text(
                    "$score",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4D1B1B)
                )
                if (bestScore != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Best: $bestScore  •  ${
                            maxOf(
                                0,
                                bestScore - score
                            )
                        } points to beat it!",
                        fontSize = 14.sp,
                        color = Color(0xFF8C5B5B)
                    )
                }

                Spacer(Modifier.height(16.dp))
                HintPill(text = randomTip())

                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onRetry,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Retry 🔄")
                    }
                    TextButton(onClick = onHome) { Text("Home") }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun HintPill(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFEEE7FF)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = Color(0xFF4A3D6C)
        )
    }
}

private fun randomTip(): String {
    val tips = listOf(
        "Focus on + and − first, then × and ÷.",
        "Use scratch paper to avoid silly mistakes!",
        "Try estimating before solving exactly.",
        "Look for patterns—math loves patterns!",
        "Breathe. Rushing causes most errors."
    )
    return tips.random()
}

// Simple confetti burst
@Composable
fun Confetti(
    modifier: Modifier = Modifier,
    particles: Int = 120,
    durationMs: Int = 2800
) {
    val transition = rememberInfiniteTransition(label = "confetti")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMs, easing = LinearEasing)),
        label = "t"
    )

    val seeds = remember { List(particles) { Random.nextFloat() } }

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        seeds.forEachIndexed { i, s ->
            val angle = s * 2f * PI.toFloat()
            val speed = 40f + (s * 120f)
            val x = (w / 2f) + cos(angle) * (time * speed * 6)
            val y = (h / 6f) + sin(angle) * (time * speed * 6) + time * h * 0.7f

            val sizePx = 6f + (s * 10f)
            val color = listOf(
                Color(0xFFFFC107), Color(0xFF8BC34A),
                Color(0xFF03A9F4), Color(0xFFE91E63),
                Color(0xFFFF5722)
            )[i % 5]

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(sizePx, sizePx / 1.6f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
            )
        }
    }
}

// Soft floating bubbles for Game Over bg
@Composable
fun FloatingBubbles(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "bubbles")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "theta"
    )
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        for (i in 0 until 20) {
            val r = 20f + (i * 3f)
            val x = (w / 2f) + cos(t + i) * (w * 0.35f)
            val y = (h / 2f) + sin(t * 0.7f + i) * (h * 0.35f)
            drawCircle(
                color = Color.White.copy(alpha = 0.18f),
                radius = r,
                center = Offset(x, y)
            )
        }
    }
}
