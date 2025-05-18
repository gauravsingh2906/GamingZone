package com.google.codelab.gamingzone.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {

    val currentBackStackEntry =
        navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(null)

    val currentDestination: NavDestination? = currentBackStackEntry.value?.destination

    val showBottomNav = BottomNavItem.entries.map { it.route::class }
        .any { route ->
            currentDestination?.hierarchy?.any {
                it.hasRoute(route)
            } == true
        }

    Log.d("BottomNavigationBar", "showBottomNav: $showBottomNav")


        BottomAppBar(
           modifier = Modifier.fillMaxWidth().height(110.dp),
        ) {
            BottomNavItem.entries.map { bottomNavigationItem ->
                var isSelecsted = currentDestination?.hierarchy?.any {
                    it.hasRoute(bottomNavigationItem.route::class)
                } == true

                if (currentDestination != null) {
                    NavigationBarItem(
                        onClick ={
                            navController.navigate(bottomNavigationItem.route)
                        },
                        icon = {
                            Icon(
                                imageVector = bottomNavigationItem.icon,
                                contentDescription = bottomNavigationItem.title
                            )
                        },
                        label = {
                            Text(text = bottomNavigationItem.title)
                        },
                        selected = isSelecsted,
                    )
                }

            }


   //     }
    }




//    NavigationBar {
//        navItems.forEachIndexed { index,item->
//           val isSelected=  if(navBarState == index) true else false
//            Log.d("BottomNavigationBar", "isSelected: $isSelected")
//            NavigationBarItem(
//                selected = isSelected,
//                onClick = {
//                    navBarState =index
//                    Log.d("BottomNavigationBar", "navBarState: $navBarState")
//                    Log.d("BottomNavigationBar", "index: $index")
//
//                    when(index) {
//                        0 -> navController.navigate(Routes.HomeScreen)
//                        1 -> navController.navigate(Routes.SearchScreen)
//                        2 -> navController.navigate(Routes.FavoritesScreen)
//                        3 -> navController.navigate(Routes.LeaderBoardScreen)
//                        4 -> navController.navigate(Routes.ProfileScreen)
//                    }
//                },
//                icon = {
//                    Icon(
//                    imageVector = item.icon,
//                    contentDescription = item.title
//                )},
//                label ={ Text(
//                    text = item.title
//                )},
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color.Red,
//                    unselectedIconColor = Color.Gray,
//                    selectedTextColor = Color.Red,
//                    unselectedTextColor = Color.Gray,
//                    indicatorColor = Color.Yellow
//                )
//            )
//        }
//    }


//    NavigationBar {
//        navItems.forEach { item ->
//
//            val last = item.route.route
//
//            val isSelected = currentRoute == last
//            Log.d("BottomNavigationBar", "isSelected: $isSelected")
//
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        imageVector = item.icon,
//                        contentDescription = item.title
//                    )
//                },
//                label = { Text(item.title) },
//                selected = isSelected,
//                alwaysShowLabel = true,
//                onClick = {
//                    if (currentRoute != item.route.route) {
//                        navController.navigate(item.route) {
//                            popUpTo(navController.graph.startDestinationId) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color.Red,
//                    unselectedIconColor = Color.Gray,
//                    selectedTextColor = Color.Red,
//                    unselectedTextColor = Color.Gray,
//                    indicatorColor = Color.Yellow
//                )
//            )
//        }
//    }
}


