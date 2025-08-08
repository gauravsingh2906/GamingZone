package com.google.codelab.gamingzone.presentation.games

import androidx.compose.runtime.Composable


@Composable
fun FearZoneScreen(viewModel: FearZoneViewModel = hiltViewModel()) {
    val tiles = viewModel.tiles
    val player = viewModel.player
    val fear = viewModel.fearLevel

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Fear Zone", color = Color.White, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        GridView(tiles)

        Spacer(Modifier.height(16.dp))

        FearMeter(fear)

        Spacer(Modifier.height(16.dp))

        ControlButtons { viewModel.movePlayer(it) }
    }
}
