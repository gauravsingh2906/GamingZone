package com.google.codelab.gamingzone.presentation.games.glitch_escape

import android.annotation.SuppressLint
import android.graphics.RectF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GlitchEscapeGameEnhanced(
    levelData: LevelData,
    onGameOver: () -> Unit,
    onVictory: () -> Unit
) {
    val tileSize = 48.dp
    val density = LocalDensity.current
    val tileSizePx = with(density) { tileSize.toPx() }

    val player = remember { mutableStateOf(Player(levelData.playerStart.first, levelData.playerStart.second)) }
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale.value = (scale.value * zoom).coerceIn(0.5f, 2.5f)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val scaledTileSize = tileSizePx * scale.value
                    val tappedX = (offset.x / scaledTileSize).toInt()
                    val tappedY = (offset.y / scaledTileSize).toInt()
                    val newX = tappedX.coerceIn(0, levelData.gridSize - 1)
                    val newY = tappedY.coerceIn(0, levelData.gridSize - 1)
                    val targetTile = grid.firstOrNull { it.x == newX && it.y == newY }
                    if (targetTile != null && !targetTile.isGlitching) {
                        player.value = player.value.copy(x = newX, y = newY)
                        scope.launch {
                            playerOffsetX.animateTo(newX * tileSizePx, tween(300))
                            playerOffsetY.animateTo(newY * tileSizePx, tween(300))
                        }
                    }
                }
            }
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
                    it.nativeCanvas.drawBitmap(portalBitmap, null, rect, null)
                }

                // Draw enemies (first frame only for now)
                for (enemyPath in levelData.enemyPaths) {
                    val (ex, ey) = enemyPath.first()
                    drawIntoCanvas {
                        val rect = RectF(
                            ex * tileSizePx,
                            ey * tileSizePx,
                            ex * tileSizePx + tileSizePx,
                            ey * tileSizePx + tileSizePx
                        )
                        it.nativeCanvas.drawBitmap(enemyBitmap, null, rect, null)
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
            }
        }
    }
}
