package com.google.codelab.gamingzone.presentation.games


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun SortColorsGame(viewModel: SortColorsViewModel = viewModel()) {
    val tubes = viewModel.tubes
    val selected = viewModel.selectedTube
    val isPouring = viewModel.isPouring
    val flightAnim = viewModel.flightAnim

    val tubeSize = 60.dp
    val tubeHeight = 180.dp
    val numTubes = tubes.size

    val boardOrigin = remember { mutableStateOf(Offset.Zero) }
    // Compute tube slot centers
    val density = LocalDensity.current
    val tubeHeightPx = with(density) { tubeHeight.toPx() }
    val tubeWidthPx = with(LocalDensity.current) { tubeSize.toPx() }
    val tubeSpacingPx = with(LocalDensity.current) { 16.dp.toPx() } + tubeWidthPx

    val tubePositions = remember { mutableStateListOf<Offset>() }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.onGloballyPositioned {
            val pos = it.localToWindow(Offset.Zero)
            boardOrigin.value = Offset(pos.x, pos.y)
        }) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                tubes.forEachIndexed { index, tube ->
                    var localOffset by remember { mutableStateOf(Offset.Zero) }
                    Box(
                        Modifier
                            .onGloballyPositioned {
                                val pos = it.localToWindow(Offset.Zero)
                                val size = it.size
                                val offset = Offset(
                                    pos.x + size.width / 2,
                                    pos.y + size.height / 2
                                )
                                localOffset = offset
                                if (tubePositions.size > index)
                                    tubePositions[index] = offset
                                else if (tubePositions.size == index)
                                    tubePositions.add(offset)
                            }
                    ) {
                        TubeView(
                            tube = tube,
                            isSelected = selected == index && !isPouring,
                            isBlocked = isPouring,
                            modifier = Modifier
                                .size(tubeSize, tubeHeight)
                                .clickable(
                                    enabled = !isPouring,
                                    onClick = {
                                        if (tubePositions.size == tubes.size) {
                                            viewModel.onTubeClick(index, tubePositions.toList())
                                        }
                                    }
                                )
                        )
                    }
                }
            }
            if (flightAnim != null && tubePositions.size == tubes.size) {
                FlightOverlayBoardRelative(
                    anim = flightAnim,
                    boardOrigin = boardOrigin.value,
                    tubeSpacing = tubeSpacingPx,
                    tubeWidth = tubeWidthPx,
                    tubeHeight = tubeHeightPx
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Text("Tap a tube, then tap where to pour.", color = Color.White)
    }
}

@Composable
fun FlightOverlayBoardRelative(
    anim: TubeFlightAnim,
    boardOrigin: Offset,
    tubeSpacing: Float,
    tubeWidth: Float,
    tubeHeight: Float,
    liftAmount: Float = tubeHeight,
    pourTilt: Float = -65f
) {
    // Positions (centerX of tube in "board" row)
    val srcX = boardOrigin.x + anim.fromIdx * tubeSpacing + tubeWidth / 2f
    val trgX = boardOrigin.x + anim.toIdx * tubeSpacing + tubeWidth / 2f
    val yOrigin = boardOrigin.y + tubeHeight / 2f

    // Animation phases
    var phase by remember { mutableStateOf(1) }
    val liftAnim = remember { Animatable(yOrigin) }
    val horizAnim = remember { Animatable(srcX) }
    val downAnim = remember { Animatable(yOrigin - liftAmount) }
    val rotAnim = remember { Animatable(0f) }
    val pourAnim = remember { Animatable(0f) }
    val returnX = srcX
    val returnY = yOrigin

    LaunchedEffect(anim) {
        liftAnim.snapTo(yOrigin)
        liftAnim.animateTo(yOrigin - liftAmount, tween(250))
        phase = 2
        horizAnim.snapTo(srcX)
        horizAnim.animateTo(trgX, tween(250))
        phase = 3
        downAnim.snapTo(liftAnim.value)
        downAnim.animateTo(yOrigin, tween(200))
        phase = 4
        rotAnim.snapTo(0f)
        rotAnim.animateTo(pourTilt, tween(200))
        phase = 5
        pourAnim.snapTo(0f)
        pourAnim.animateTo(1f, tween(400))
        phase = 6
        rotAnim.animateTo(0f, tween(200))
        horizAnim.animateTo(returnX, tween(250))
        downAnim.animateTo(returnY, tween(250))
        phase = 0
    }

    val x = when (phase) {
        1,2 -> horizAnim.value
        3,4,5,6 -> trgX
        else -> returnX
    }
    val y = when (phase) {
        1 -> liftAnim.value
        2 -> liftAnim.value
        3,4,5,6 -> downAnim.value
        else -> returnY
    }
    val rotation = if (phase in 4..6) rotAnim.value else 0f

    fun mouthOffset(rotation: Float): Offset {
        val mouth = Offset(tubeWidth / 2f, 12f)
        val pivot = Offset(tubeWidth / 2f, tubeHeight / 2f)
        val angleRad = rotation * Math.PI.toFloat() / 180f
        val dx = mouth.x - pivot.x
        val dy = mouth.y - pivot.y
        val cosA = cos(angleRad)
        val sinA = sin(angleRad)
        val rotated = Offset(
            x = pivot.x + dx * cosA - dy * sinA,
            y = pivot.y + dx * sinA + dy * cosA
        )
        return rotated
    }
    val startMouth = mouthOffset(rotation)
    val targetMouth = Offset(tubeWidth / 2f, 12f)

    Box(
        Modifier
            .absoluteOffset { IntOffset((x - tubeWidth / 2f).roundToInt(), (y - tubeHeight / 2f).roundToInt()) }
            .size(tubeWidth.dp, tubeHeight.dp)
            .zIndex(10f)
            .graphicsLayer { rotationZ = rotation },
        contentAlignment = Alignment.BottomCenter
    ) {
        TubeView(
            tube = anim.tube,
            isSelected = true,
            isBlocked = true
        )
        if (phase == 5) {
            PourStreamCurved(
                color = anim.colorToPour,
                mouthStart = startMouth,
                mouthEnd = targetMouth,
                progress = pourAnim.value
            )
        }
    }
}

@Composable
fun PourStreamCurved(
    color: Color,
    mouthStart: Offset,
    mouthEnd: Offset,
    progress: Float
) {
    Canvas(Modifier.fillMaxSize()) {
        val t = progress
        val mid = Offset((mouthStart.x + mouthEnd.x) / 2f, minOf(mouthStart.y, mouthEnd.y) - 32f)
        val currEnd = quadraticBezier(mouthStart, mid, mouthEnd, t)
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(mouthStart.x, mouthStart.y)
            quadraticBezierTo(mid.x, mid.y, currEnd.x, currEnd.y)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 16f, cap = StrokeCap.Round)
        )
        drawCircle(color = color, center = currEnd, radius = 8f)
    }
}

@Composable
fun TubeView(
    tube: Tube,
    isSelected: Boolean,
    isBlocked: Boolean,
    modifier: Modifier = Modifier
) {
    val offsetY by animateDpAsState(
        if (isSelected) (-30).dp else 0.dp,
        animationSpec = tween(300)
    )
    Box(
        modifier
            .offset(y = offsetY)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.DarkGray.copy(alpha = 0.45f))
            .border(3.dp, Color.White, RoundedCornerShape(18.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            repeat(4) { i ->
                val color = if (i < tube.colors.size) tube.colors[i] else Color.Transparent
                FillSegment(color)
            }
        }
    }
}

@Composable
fun FillSegment(color: Color) {
    val heightProgress by animateFloatAsState(
        targetValue = if (color != Color.Transparent) 1f else 0f,
        animationSpec = tween(300)
    )
    Box(
        Modifier
            .height(34.dp * heightProgress + 4.dp)
            .fillMaxWidth()
            .background(color, RoundedCornerShape(10.dp))
            .padding(vertical = 1.dp)
    )
}

fun quadraticBezier(start: Offset, control: Offset, end: Offset, t: Float): Offset {
    val u = 1 - t
    val x = u * u * start.x + 2 * u * t * control.x + t * t * end.x
    val y = u * u * start.y + 2 * u * t * control.y + t * t * end.y
    return Offset(x, y)
}