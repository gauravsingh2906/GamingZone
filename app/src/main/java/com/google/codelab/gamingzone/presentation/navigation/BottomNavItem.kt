package com.google.codelab.gamingzone.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Routes
) {
    Home(
        title = "Home",
        icon = Icons.Default.Home,
        route = Routes.HomeScreen
    ),
    Leaderboard(
        title = "Leaderboard",
        icon = Icons.Default.Star,
        route = Routes.LeaderBoardScreen
    ),
    Profile(
        title = "Profile",
        icon = Icons.Default.Person,
        route = Routes.ProfileScreen
    )

}

