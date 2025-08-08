package com.google.codelab.gamingzone.presentation.games.masyu_puzzle_japan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.codelab.gamingzone.domain.masyu_puzzle.Dot
import com.google.codelab.gamingzone.domain.masyu_puzzle.DotType
import kotlin.math.abs

@Composable
fun SymbolLogicGrid(
    gridSize: Int = 7,
    initialDots: List<Dot> = emptyList()
) {
    val cellSize = 48.dp
    val pathPoints = remember { mutableStateListOf<Pair<Int, Int>>() }

    val loopClosed = remember { mutableStateOf(false) }
    val validPath = remember { mutableStateOf(false) }







    Box(
        modifier = Modifier
            .size(cellSize * gridSize)
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellPx = size.width / gridSize

            // Draw grid lines
            for (i in 0..gridSize) {
                drawLine(Color.Black, Offset(i * cellPx, 0f), Offset(i * cellPx, size.height))
                drawLine(Color.Black, Offset(0f, i * cellPx), Offset(size.width, i * cellPx))
            }

            // Draw white/black dots
            initialDots.forEach { cell ->
                val cx = (cell.col + 0.5f) * cellPx
                val cy = (cell.row + 0.5f) * cellPx
                val color = when (cell.dot) {
                    DotType.BLACK -> Color.Black
                    DotType.WHITE -> Color.White
                    null -> return@forEach
                }
                drawCircle(
                    color = Color.White,
                    radius = cellPx / 6,
                    center = Offset(cx, cy),
                    style = Stroke(width = 3f)
                )
                drawCircle(
                    color = Color.Black,
                    radius = cellPx / 6,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1f)
                )

            }

            // Draw path
            for (i in 1 until pathPoints.size) {
                val (x1, y1) = pathPoints[i - 1]
                val (x2, y2) = pathPoints[i]
                drawLine(
                    color = Color.Magenta,
                    start = Offset((x1 + 0.5f) * cellPx, (y1 + 0.5f) * cellPx),
                    end = Offset((x2 + 0.5f) * cellPx, (y2 + 0.5f) * cellPx),
                    strokeWidth = 6f
                )
            }

            if (loopClosed.value) {
                drawContext.canvas.nativeCanvas.drawText(
                    if (validPath.value) "✅ Loop & Rules OK!" else "❌ Dot Rule Error!",
                    20f,
                    size.height + 50f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.GREEN
                        textSize = 40f
                    }
                )
            }

        }
        val (isValid, ruleHints) = validateDotRules(pathPoints.toList(), initialDots)

        if (!isValid && loopClosed.value) {
            ruleHints.forEach {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        }

        // Tapping to add path nodes
        Box(modifier = Modifier
            .matchParentSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                val cellPx = size.width / gridSize
                val x = (offset.x / cellPx).toInt()
                val y = (offset.y / cellPx).toInt()
                val current = x to y

                val last = pathPoints.lastOrNull()
                if (last == null || isAdjacent(last, current) || current == pathPoints.first()) {
                    pathPoints.add(current)

                    // Check if loop is closed
                    if (pathPoints.size >= 4 && current == pathPoints.first()) {
                        loopClosed.value = true
                        val (valid, hints) = validateDotRules(pathPoints, initialDots)
                        validPath.value = valid
                        println(hints.joinToString("\n"))
                    }
                }
            }

            }
        )
    }
}

fun isAdjacent(a: Pair<Int, Int>, b: Pair<Int, Int>): Boolean {
    val (x1, y1) = a
    val (x2, y2) = b
    return (x1 == x2 && abs(y1 - y2) == 1) || (y1 == y2 && abs(x1 - x2) == 1)
}

fun isLoopClosed(path: List<Pair<Int, Int>>): Boolean {
    if (path.size < 4) return false
    return path.first() == path.last()
}

fun validateDotRules(
    path: List<Pair<Int, Int>>,
    dots: List<Dot>
): Pair<Boolean, List<String>> {
    val hints = mutableListOf<String>()
    val indexedPath = path.withIndex().associate { (i, p) -> p to i }

    for (dot in dots) {
        val index = indexedPath[dot.row to dot.col] ?: continue

        val prev = path.getOrNull(index - 1)
        val curr = path[index]
        val next = path.getOrNull(index + 1)

        val dir1 = direction(prev, curr)
        val dir2 = direction(curr, next)

        when (dot.dot) {
            DotType.WHITE -> {
                if (dir1 != dir2) {
                    val preTurn = direction(path.getOrNull(index - 2), prev)
                    val postTurn = direction(next, path.getOrNull(index + 2))
                    if (preTurn == dir1 && postTurn == dir1) {
                        hints.add("White dot at (${dot.row}, ${dot.col}) must turn before or after.")
                    }
                } else {
                    hints.add("White dot at (${dot.row}, ${dot.col}) must turn.")
                }
            }
            DotType.BLACK -> {
                if (dir1 == dir2) {
                    hints.add("Black dot at (${dot.row}, ${dot.col}) must turn.")
                }
                val preStraight = direction(path.getOrNull(index - 2), prev)
                val postStraight = direction(next, path.getOrNull(index + 2))
                if (preStraight != dir1 || postStraight != dir2) {
                    hints.add("Black dot at (${dot.row}, ${dot.col}) must go straight before and after.")
                }
            }
            null -> {}
        }
    }

    return Pair(hints.isEmpty(), hints)
}


fun direction(from: Pair<Int, Int>?, to: Pair<Int, Int>?): String? {
    if (from == null || to == null) return null
    return when {
        from.first == to.first && from.second == to.second + 1 -> "L"
        from.first == to.first && from.second == to.second - 1 -> "R"
        from.first == to.first + 1 && from.second == to.second -> "U"
        from.first == to.first - 1 && from.second == to.second -> "D"
        else -> null
    }
}

