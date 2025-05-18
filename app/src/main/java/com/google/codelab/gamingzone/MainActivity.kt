package com.google.codelab.gamingzone

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import com.google.codelab.gamingzone.presentation.navigation.BottomNavItem
import com.google.codelab.gamingzone.presentation.navigation.NavGraph
import com.google.codelab.gamingzone.presentation.ui.theme.GamingZoneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamingZoneTheme {
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                var showBottomBar by remember { mutableStateOf(true) }
                var previousOffset by remember { mutableFloatStateOf(0f) }

                val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(null)
                val currentDestination = currentBackStackEntry?.destination

                val shouldShowBottomNav = remember(currentDestination) {
                    val shouldShow = BottomNavItem.entries.any { item ->
                        val currentRoutes = currentDestination?.hierarchy?.mapNotNull { it.route }.toString()
                        val bottomItemRoute = item.route.toString()
                        val matches = currentDestination?.hierarchy?.any { entry ->
                            val entryRoute = entry.route
                            val endsWith = entryRoute?.endsWith(bottomItemRoute) == true
                            Log.d("Navigation", "Checking ${item.title}: Current Entry Route: $entryRoute, Bottom Item Route: $bottomItemRoute, EndsWith: $endsWith")
                            endsWith
                        } == true
                        matches
                    }
                    Log.d("Navigation", "Final shouldShowBottomNav for ${currentDestination?.route}: $shouldShow")
                    shouldShow
                }

                LaunchedEffect(shouldShowBottomNav, scrollBehavior.state) {
                    if (shouldShowBottomNav) {
                        previousOffset = scrollBehavior.state.contentOffset
                        snapshotFlow { scrollBehavior.state.contentOffset }
                            .distinctUntilChanged()
                            .onEach { currentOffset ->
                                val delta = currentOffset - previousOffset
                                val scrollThreshold = 1f

                                Log.d("MainActivity", "Offset Listener - Current: $currentOffset, Previous: $previousOffset, Delta: $delta")

                                if (delta < scrollThreshold) {
                                    // Scrolling down
                                    Log.d("MainActivity", "Offset Listener - Scrolling DOWN, hiding bottom bar")
                                    showBottomBar = false
                                } else if (delta > -scrollThreshold) {
                                    // Scrolling up (we don't want to hide immediately)
                                    Log.d("MainActivity", "Offset Listener - Scrolling UP")
                                    // Optionally, you could add logic here if you want immediate action on scrolling up
                                }
                                previousOffset = currentOffset
                            }
                            .debounce(0) // Wait for 150ms of no new offset values
                            .collectLatest {
                                Log.d("MainActivity", "Debounce - Scrolling STOPPED, showing bottom bar")
                                showBottomBar = true
                            }
                    } else {
                        showBottomBar = false
                        previousOffset = 0f
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    bottomBar = {
                        Log.d("MainActivity", "shouldShowBottomNav (in bottomBar): $shouldShowBottomNav")
                        Log.d("MainActivity", "showBottomBar (in bottomBar): $showBottomBar")
                        AnimatedVisibility(
                            visible = shouldShowBottomNav && showBottomBar,
                            enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                                    slideInVertically(initialOffsetY = { it }, animationSpec = tween(durationMillis = 300)),
                            exit = fadeOut(animationSpec = tween(durationMillis = 200)) +
                                    slideOutVertically(targetOffsetY = { it }, animationSpec = tween(durationMillis = 200)) +
                                    shrinkVertically(animationSpec = tween(durationMillis = 200))
                        ) {
                            BottomAppBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                containerColor = Color(0xFFEEF6FF),
                                tonalElevation = 0.dp
                            ) {
                                BottomNavItem.entries.forEach { bottomNavigationItem ->
                                    val isSelected = currentDestination?.hierarchy?.any { entry ->
                                        entry.route == bottomNavigationItem.route.toString()
                                    } == true

                                    NavigationBarItem(
                                        onClick = {
                                            navController.navigate(bottomNavigationItem.route)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = bottomNavigationItem.icon,
                                                contentDescription = bottomNavigationItem.title,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .scale(if (isSelected) 1.3f else 1f),
                                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                            )
                                        },
                                        label = {
                                            Text(text = bottomNavigationItem.title)
                                        },
                                        alwaysShowLabel = true,
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                        ),
                                        selected = isSelected,
                                    )
                                }
                            }
                        }
                    }
                ) {
                    NavGraph(
                        navController = navController,
                        scrollBehavior = scrollBehavior,
                        paddingValues = it
                    )
                }
            }
        }
    }
}