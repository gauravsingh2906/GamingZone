package com.google.codelab.gamingzone.tobe


import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun TubeScreen(viewModel: TubeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    val tubePositions = remember { mutableStateListOf<Offset>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Level ${state.level}", style = MaterialTheme.typography.headlineMedium)
            if (state.won) {
                Text("🎉 You Won!", style = MaterialTheme.typography.headlineSmall)
                Button(onClick = { viewModel.onEvent(GameEvent.NextLevel) }) {
                    Text("Next Level")
                }
            }
        }


        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(state.tubes.size) { index ->
                val tube = state.tubes[index]
                val isSelected = index == state.selectedTubeIndex


                Box(
                    modifier = Modifier
                        .onGloballyPositioned {
                            val offset = it.positionInRoot()
                            if (tubePositions.size <= index) {
                                tubePositions.add(offset)
                            } else {
                                tubePositions[index] = offset
                            }
                        }
                ) {
                    TubeView(
                        tube = tube,
                        isSelected = isSelected,
                        onClick = { viewModel.onEvent(GameEvent.TubeClicked(index)) }
                    )
                }
            }
        }




        Spacer(Modifier.height(12.dp))

        if (state.won) {
            Text("🎉 You Won!", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { viewModel.onEvent(GameEvent.NextLevel) }) {
                Text("Next Level")
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
//                OutlinedButton(onClick = { viewModel.onEvent(GameEvent.Undo) }) {
//                    Text("Undo")
//                }
//                OutlinedButton(onClick = { viewModel.onEvent(GameEvent.Redo) }) {
//                    Text("Redo")
//                }
                Button(onClick = { viewModel.onEvent(GameEvent.RestartGame) }) {
                    Text("Restart")
                }
            }
        }
    }
}


@Composable
fun TubeView(
    tube: Tube,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color.Black else Color.Gray

    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 180.dp)
            .clip(RoundedCornerShape(40.dp))
            .border(3.dp, borderColor, RoundedCornerShape(40.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .clickable { onClick() }
            .padding(6.dp),
        contentAlignment = Alignment.TopCenter // ✅ Changed from BottomCenter
    ) {
        Column(
            verticalArrangement = Arrangement.Top, // ✅ Changed from Bottom
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
//                val maxCapacity = Tube.MAX_CAPACITY
//                val emptySlots = maxCapacity - tube.balls.size
//
//                repeat(emptySlots) {
//                    AnimatedBallSlot(color = null)
//                }
//
//                tube.balls.forEach { color ->
//                    AnimatedBallSlot(color = color)
//                }
                val reversedBalls = tube.balls.reversed()
                for (i in 0 until TubeViewModel.maxTubeCapacity) {
                    val color = reversedBalls.getOrNull(i)
                    AnimatedBallSlot(color = color)
                }
            }
        }
    }
}


@Composable
fun AnimatedBallSlot(color: Color?) {
    val ballModifier = Modifier
        .size(40.dp)
        .padding(vertical = 2.dp)

    Box(
        modifier = ballModifier,
        contentAlignment = Alignment.Center
    ) {
        if (color != null) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        } else {
            // Empty slot
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.2f))
            )
        }
    }
}

@Composable
fun AnimatedTransferBall(
    startOffset: Offset,
    endOffset: Offset,
    color: Color,
    onAnimationEnd: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }
    val animatedOffset by animateOffsetAsState(
        targetValue = if (visible) endOffset else startOffset,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        finishedListener = {
            visible = false
            onAnimationEnd()
        },
        label = "Ball Transfer"
    )

    if (visible) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer()
        ) {
            drawCircle(
                color = color,
                radius = 20f,
                center = animatedOffset
            )
        }
    }
}






//@Composable
//fun TubeViewer(
//    tube: Tube,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    pouringFrom: Boolean,
//    pouringTo: Boolean,
//    onAnimationFinished: () -> Unit
//) {
//    val borderColor = if (isSelected) Color.Magenta else Color.Gray
//
//    Column(
//        modifier = Modifier
//            .clickable(enabled = !pouringFrom && !pouringTo, onClick = onClick)
//            .width(60.dp)
//            .height(180.dp)
//            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
//            .background(Color.White, RoundedCornerShape(12.dp))
//            .padding(4.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        val tubeColors = tube.colors.toList()
//        val emptySlots = TubeViewModel.maxTubeCapacity - tubeColors.size
//
//        repeat(emptySlots) {
//            Spacer(modifier = Modifier
//                .padding(2.dp)
//                .size(40.dp))
//        }
//
//        tubeColors.forEach { color ->
//            Box(
//                modifier = Modifier
//                    .padding(2.dp)
//                    .size(40.dp)
//                    .clip(CircleShape)
//                    .background(color)
//            )
//        }
//    }
//
//    // Simulate simple delay to call animation finish
//    if (pouringTo) {
//        LaunchedEffect(Unit) {
//            delay(250) // simulate animation
//            onAnimationFinished()
//        }
//    }
//}







