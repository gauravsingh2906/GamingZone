package com.google.codelab.gamingzone.presentation.games.glitch_escape

import android.annotation.SuppressLint
import android.graphics.RectF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.codelab.gamingzone.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun GlitchEscapeApp() {
    val currentScreen = remember { mutableStateOf(Screen.MENU) }
    val currentLevel = remember { mutableStateOf<LevelData?>(null) }
    val difficulty = remember { mutableStateOf<GlitchDifficulty?>(null) }
    val maxUnlockedLevel = remember { mutableStateOf(0) }

    when (currentScreen.value) {
        Screen.MENU -> MainMenu {
            currentScreen.value = Screen.DIFFICULTY
        }

        Screen.DIFFICULTY -> DifficultySelectorScreen {
            difficulty.value = it
            currentScreen.value = Screen.LEVEL_SELECT
        }

        Screen.LEVEL_SELECT -> LevelSelectorScreen(
            maxUnlockedLevel = maxUnlockedLevel.value,
            difficulty = difficulty.value!!,
            onLevelSelected = {
                currentLevel.value = it
                currentScreen.value = Screen.GAME
            }
        )

        Screen.GAME -> currentLevel.value?.let { level ->
            GlitchEscapeGame(
                levelData = level,
                onGameOver = { currentScreen.value = Screen.GAME_OVER },
                onVictory = {
                    // unlock next level
                    if (level.levelNumber >= maxUnlockedLevel.value) {
                        maxUnlockedLevel.value = level.levelNumber + 1
                    }
                    currentScreen.value = Screen.VICTORY
                }
            )
        }

        Screen.GAME_OVER -> GameOverScreen {
            currentScreen.value = Screen.MENU
        }

        Screen.VICTORY -> VictoryScreen {
            currentScreen.value = Screen.LEVEL_SELECT
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GlitchEscapeGame(
    levelData: LevelData,
    onGameOver: () -> Unit,
    onVictory: () -> Unit
) {
    val tileSize = 48.dp
    val density = LocalDensity.current
    val tileSizePx = with(density) { tileSize.toPx() }

    val steps = remember { mutableStateOf(0) }
    val timer = remember { mutableStateOf(0) }

    val enemyFrames = listOf(
        ImageBitmap.imageResource(R.drawable.enemy_frame_0),
        ImageBitmap.imageResource(R.drawable.enemy_frame_1),
    )

    val portalFrames = listOf(
        ImageBitmap.imageResource(R.drawable.portal_frame_0),
        ImageBitmap.imageResource(R.drawable.portal_frame_1),
        ImageBitmap.imageResource(R.drawable.portal_frame_2),
        ImageBitmap.imageResource(R.drawable.portal_frame_3),
        ImageBitmap.imageResource(R.drawable.portal_frame_4),
        ImageBitmap.imageResource(R.drawable.portal_frame_5),
    )

    val flashFrames = listOf(
        ImageBitmap.imageResource(R.drawable.flash_frame_0),
        ImageBitmap.imageResource(R.drawable.flash_frame_1),
        ImageBitmap.imageResource(R.drawable.flash_frame_2),
    )

    val enemyFrameIndex = remember { mutableStateOf(0) }
    val portalFrameIndex = remember { mutableStateOf(0) }
    val flashFrameIndex = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(150)
            enemyFrameIndex.value = (enemyFrameIndex.value + 1) % enemyFrames.size
            portalFrameIndex.value = (portalFrameIndex.value + 1) % portalFrames.size
            flashFrameIndex.value = (flashFrameIndex.value + 1) % flashFrames.size
        }
    }


    //  val player = remember { mutableStateOf(Player(levelData.playerStart.first, levelData.playerStart.second)) }


    val player = remember {
        mutableStateOf(
            Player(
                levelData.playerStart.first,
                levelData.playerStart.second
            )
        )
    }
    val playerOffsetX = remember { Animatable(player.value.x * tileSizePx) }
    val playerOffsetY = remember { Animatable(player.value.y * tileSizePx) }

    val scale = remember { mutableStateOf(1f) }
    val scope = rememberCoroutineScope()

    val portalBitmap = ImageBitmap.imageResource(id = R.drawable.portal1).asAndroidBitmap()
    val playerBitmap = ImageBitmap.imageResource(id = R.drawable.player1).asAndroidBitmap()
    val enemyBitmap = ImageBitmap.imageResource(id = R.drawable.enemy1).asAndroidBitmap()

    val grid = remember {
        List(levelData.gridSize * levelData.gridSize) {
            val x = it % levelData.gridSize
            val y = it / levelData.gridSize
            val isGlitch = levelData.glitchTiles.contains(x to y)
            GridTile(x, y, isGlitching = isGlitch)
        }.toMutableStateList()
    }

    val enemies = remember {
        mutableStateListOf<AnimatedEnemy>().apply {
            levelData.enemyPaths.forEach { path ->
                val (startX, startY) = path.first()
                add(
                    AnimatedEnemy(
                        x = startX,
                        y = startY,
                        path = path,
                        offsetX = Animatable(startX * tileSizePx),
                        offsetY = Animatable(startY * tileSizePx)
                    )
                )
            }
        }
    }


    val gameState = remember {
        mutableStateOf(GameState(grid, player.value, enemies))
    }

    LaunchedEffect(Unit) {
        while (!gameState.value.isGameOver && !gameState.value.isGameWon) {
            delay(1000)
            timer.value += 1
        }
    }

    // 👾 Glitch tile animation
    LaunchedEffect(Unit) {
        while (!gameState.value.isGameOver && !gameState.value.isGameWon) {
            delay(800)
            val glitchTiles = grid.shuffled().take(4)
            glitchTiles.forEachIndexed { i, tile ->
                val index = tile.y * levelData.gridSize + tile.x
                if (index in grid.indices) {
                    grid[index] = tile.copy(isGlitching = i % 2 == 0)
                }
            }
        }
    }

    // 👿 Enemy movement
    LaunchedEffect(Unit) {
        while (!gameState.value.isGameOver && !gameState.value.isGameWon) {
            delay(600)
            enemies.forEach { enemy ->
                enemy.pathIndex = (enemy.pathIndex + 1) % enemy.path.size
                val (nx, ny) = enemy.path[enemy.pathIndex]
                scope.launch {
                    enemy.offsetX.animateTo(nx * tileSizePx, tween(300))
                    enemy.offsetY.animateTo(ny * tileSizePx, tween(300))
                }
                enemy.x = nx
                enemy.y = ny
            }
            if (enemies.any { it.x == player.value.x && it.y == player.value.y }) {
                gameState.value = gameState.value.copy(isGameOver = true)
                onGameOver()
            }
        }
    }

    // 🎯 Win / lose condition
    LaunchedEffect(player.value) {
        val currentTile = grid.firstOrNull { it.x == player.value.x && it.y == player.value.y }
        if (currentTile?.isGlitching == true) {
            gameState.value = gameState.value.copy(isGameOver = true)
            onGameOver()
        } else if (player.value.x == levelData.portal.first && player.value.y == levelData.portal.second) {
            gameState.value = gameState.value.copy(isGameWon = true)
            onVictory()
        }
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale.value = (scale.value * zoom).coerceIn(0.5f, 2.5f)
                }
            }
//            .pointerInput(Unit) {
//                detectTapGestures { offset ->
//                    val scaledTileSize = tileSizePx * scale.value
//                    val tappedX = (offset.x / scaledTileSize).toInt()
//                    val tappedY = (offset.y / scaledTileSize).toInt()
//                    val newX = tappedX.coerceIn(0, levelData.gridSize - 1)
//                    val newY = tappedY.coerceIn(0, levelData.gridSize - 1)
//                    val targetTile = grid.firstOrNull { it.x == newX && it.y == newY }
//                    if (targetTile != null && !targetTile.isGlitching) {
//                        player.value = player.value.copy(x = newX, y = newY)
//                        scope.launch {
//                            playerOffsetX.animateTo(newX * tileSizePx, tween(300))
//                            playerOffsetY.animateTo(newY * tileSizePx, tween(300))
//                        }
//                    }
//                }
//            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Level ${levelData.levelNumber + 1}", color = Color.White)
                Text("Steps: ${steps.value}", color = Color.White)
                Text("⏱️ ${timer.value}s", color = Color.White)
                //   Text("⏱️ ${}s", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scaledTileSize = tileSizePx * scale.value
                    val offsetX = (playerOffsetX.value - size.width / 2f).coerceAtLeast(0f)
                    val offsetY = (playerOffsetY.value - size.height / 2f).coerceAtLeast(0f)

                    withTransform({
                        translate(-offsetX, -offsetY)
                        scale(scale.value)
                    }) {
                        // Draw glitch tiles
                        grid.forEach { tile ->
                            drawRect(
                                color = if (tile.isGlitching) Color.Red else Color.DarkGray,
                                topLeft = Offset(tile.x * tileSizePx, tile.y * tileSizePx),
                                size = androidx.compose.ui.geometry.Size(tileSizePx, tileSizePx)
                            )
                        }

                        // Draw portal
                        drawIntoCanvas {
                            val (px, py) = levelData.portal
                            val rect = RectF(
                                px * tileSizePx,
                                py * tileSizePx,
                                px * tileSizePx + tileSizePx,
                                py * tileSizePx + tileSizePx
                            )
                            it.nativeCanvas.drawBitmap(
                                portalFrames[portalFrameIndex.value].asAndroidBitmap(),
                                null,
                                rect,
                                null
                            )
                        }


                        // Draw enemies (first frame only for now)
                        enemies.forEach {
                            drawIntoCanvas { canvas ->
                                val rect = RectF(
                                    it.offsetX.value,
                                    it.offsetY.value,
                                    it.offsetX.value + tileSizePx,
                                    it.offsetY.value + tileSizePx
                                )
                                canvas.nativeCanvas.drawBitmap(
                                    enemyFrames[enemyFrameIndex.value].asAndroidBitmap(),
                                    null,
                                    rect,
                                    null
                                )
                            }
                        }

                        // Draw player
                        drawIntoCanvas {
                            val rect = RectF(
                                playerOffsetX.value,
                                playerOffsetY.value,
                                playerOffsetX.value + tileSizePx,
                                playerOffsetY.value + tileSizePx
                            )
                            it.nativeCanvas.drawBitmap(playerBitmap, null, rect, null)
                        }

                        if (gameState.value.isGameOver) {
                            drawIntoCanvas {
                                val rect = RectF(
                                    playerOffsetX.value,
                                    playerOffsetY.value,
                                    playerOffsetX.value + tileSizePx,
                                    playerOffsetY.value + tileSizePx
                                )
                                it.nativeCanvas.drawBitmap(
                                    flashFrames[flashFrameIndex.value].asAndroidBitmap(),
                                    null,
                                    rect,
                                    null
                                )
                            }
                        }

                    }

                }
            }

            // ⬇️ Controls
            DirectionControls(
                onDirection = { dx, dy ->
                    val newX = (player.value.x + dx).coerceIn(0, levelData.gridSize - 1)
                    val newY = (player.value.y + dy).coerceIn(0, levelData.gridSize - 1)

                    // Prevent move into glitch tile
                    val targetTile = grid.firstOrNull { it.x == newX && it.y == newY }
                    if (targetTile != null && !targetTile.isGlitching) {
                        steps.value++
                        player.value = player.value.copy(x = newX, y = newY)
                        scope.launch {
                            playerOffsetX.animateTo(newX * tileSizePx, tween(300))
                            playerOffsetY.animateTo(newY * tileSizePx, tween(300))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

        }
    }
}


//@SuppressLint("UnusedBoxWithConstraintsScope")
//@Composable
//fun GlitchEscapeGame(
//    levelData: LevelData,
//    onGameOver: () -> Unit,
//    onVictory: () -> Unit
//) {
//    val steps = remember { mutableStateOf(0) }
//    val timer = remember { mutableStateOf(0) }
//
//    val player = remember { mutableStateOf(Player(levelData.playerStart.first, levelData.playerStart.second)) }
//    val enemies = remember {
//        mutableStateListOf<Enemy>().apply {
//            levelData.enemyPaths.forEach { path ->
//                val (startX, startY) = path.first()
//                add(Enemy(startX, startY, path))
//            }
//        }
//    }
//
//    val grid = remember {
//        List(levelData.gridSize * levelData.gridSize) {
//            val x = it % levelData.gridSize
//            val y = it / levelData.gridSize
//            val isGlitch = levelData.glitchTiles.contains(x to y)
//            GridTile(x, y, isGlitching = isGlitch)
//        }.toMutableStateList()
//    }
//
//    val gameState = remember {
//        mutableStateOf(GameState(grid, player.value, enemies))
//    }
//
//    // ⏱ TIMER
//    LaunchedEffect(Unit) {
//        while (!gameState.value.isGameOver && !gameState.value.isGameWon) {
//            delay(1000)
//            timer.value += 1
//        }
//    }
//
//    // 👾 Glitch tile animation
//    LaunchedEffect(Unit) {
//        while (!gameState.value.isGameOver && !gameState.value.isGameWon) {
//            delay(800)
//            val glitchTiles = grid.shuffled().take(4)
//            glitchTiles.forEachIndexed { i, tile ->
//                val index = tile.y * levelData.gridSize + tile.x
//                if (index in grid.indices) {
//                    grid[index] = tile.copy(isGlitching = i % 2 == 0)
//                }
//            }
//        }
//    }
//
//    // 👿 Enemy movement
//    LaunchedEffect(Unit) {
//        while (!gameState.value.isGameOver && !gameState.value.isGameWon) {
//            delay(600)
//            enemies.forEach { enemy ->
//                enemy.pathIndex = (enemy.pathIndex + 1) % enemy.path.size
//                val (nx, ny) = enemy.path[enemy.pathIndex]
//                enemy.x = nx
//                enemy.y = ny
//            }
//            if (enemies.any { it.x == player.value.x && it.y == player.value.y }) {
//                gameState.value = gameState.value.copy(isGameOver = true)
//                onGameOver()
//            }
//        }
//    }
//
//    // 🎯 Win / lose condition
//    LaunchedEffect(player.value) {
//        val currentTile = grid.firstOrNull { it.x == player.value.x && it.y == player.value.y }
//        if (currentTile?.isGlitching == true) {
//            gameState.value = gameState.value.copy(isGameOver = true)
//            onGameOver()
//        } else if (player.value.x == levelData.portal.first && player.value.y == levelData.portal.second) {
//            gameState.value = gameState.value.copy(isGameWon = true)
//            onVictory()
//        }
//    }
//
//    // 🖼️ UI Layout
//    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
//        // 🔼 HUD
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .background(Color.DarkGray)
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text("Level ${levelData.levelNumber + 1}", color = Color.White)
//            Text("Steps: ${steps.value}", color = Color.White)
//            Text("⏱️ ${timer.value}s", color = Color.White)
//         //   Text("⏱️ ${}s", color = Color.White)
//        }
//
//        // 🧱 Game Canvas
//        BoxWithConstraints(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//                .background(Color.Black),
//            contentAlignment = Alignment.Center
//        ) {
//            val tileSize = minOf(
//                maxWidth / levelData.gridSize,
//                maxHeight / (levelData.gridSize + 2) // leave room for controls
//            )
//
//            Canvas(modifier = Modifier.size(tileSize * levelData.gridSize)) {
//                val sizePx = tileSize.toPx()
//
//                // Draw glitch and normal tiles
//                grid.forEach { tile ->
//                    drawRect(
//                        color = if (tile.isGlitching) Color.Red else Color.Gray,
//                        topLeft = Offset(tile.x * sizePx, tile.y * sizePx),
//                        size = Size(sizePx, sizePx)
//                    )
//                }
//
//                // Portal
//                drawRect(
//                    color = Color.Green,
//                    topLeft = Offset(levelData.portal.first * sizePx, levelData.portal.second * sizePx),
//                    size = Size(sizePx, sizePx)
//                )
//
//                // Enemies
//                enemies.forEach {
//                    drawRect(
//                        color = Color.Magenta,
//                        topLeft = Offset(it.x * sizePx, it.y * sizePx),
//                        size = Size(sizePx, sizePx)
//                    )
//                }
//
//                // Player
//                drawRect(
//                    color = Color.Cyan,
//                    topLeft = Offset(player.value.x * sizePx, player.value.y * sizePx),
//                    size = Size(sizePx, sizePx)
//                )
//            }
//        }
//
//        // ⬇️ Controls
//        DirectionControls(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp),
//            onDirection = { dx, dy ->
//                val newX = (player.value.x + dx).coerceIn(0, levelData.gridSize - 1)
//                val newY = (player.value.y + dy).coerceIn(0, levelData.gridSize - 1)
//                player.value = player.value.copy(x = newX, y = newY)
//                steps.value++
//            },
//
//        )
//    }
//}


// --- Main Menu ---
@Composable
fun MainMenu(onStart: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Glitch Escape", fontSize = 32.sp, color = Color.White)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onStart) { Text("Start Game") }
    }
}

// --- Victory / Game Over Screens ---
@Composable
fun VictoryScreen(onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("You Escaped!", color = Color.Green, fontSize = 28.sp)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBack) { Text("Main Menu") }
        }
    }
}

@Composable
fun GameOverScreen(onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Game Over", color = Color.Red, fontSize = 28.sp)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBack) { Text("Try Again") }
        }
    }
}

@Composable
fun DirectionControls(
    onDirection: (dx: Int, dy: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { onDirection(0, -1) }) { Text("↑") }
        Row {
            Button(onClick = { onDirection(-1, 0) }) { Text("←") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onDirection(1, 0) }) { Text("→") }
        }
        Button(onClick = { onDirection(0, 1) }) { Text("↓") }
    }
}



