package com.google.codelab.gamingzone

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToHomeScreen: ()-> Unit
) {
    val scale = remember { Animatable(0f) }
    val isDarkTheme = isSystemInDarkTheme()
    val logo = if (isDarkTheme) R.drawable.splash_dark else R.drawable.splash_light

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis =2000,
                easing = FastOutSlowInEasing
            )
        )
        delay(1000) // Wait 1.5 sec after animation
        navigateToHomeScreen()
    }

    Box(modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color.Black else Color.White), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = logo),
            contentDescription = "Brainy Quest Logo",
            modifier = Modifier
                .scale(scale.value)
                .size(200.dp)
        )
    }
}

