package com.google.codelab.gamingzone.presentation.games.math_memory

import android.util.Log

// Generates procedural levels
class LevelManager(
    val levelGenerator: (Int) -> MemoryLevel
) {
    private var currentLevelNumber = 1
    private var cachedLevel: MemoryLevel? = null

    fun currentLevel(): MemoryLevel {
        if (cachedLevel == null) {
            cachedLevel = levelGenerator(currentLevelNumber)
        }
        return cachedLevel!!
    }
    fun nextLevel() {
        currentLevelNumber++
        cachedLevel = null
    }
    fun reset() {
        currentLevelNumber = 1
        cachedLevel = null
    }
}

// Example procedural generator function
//fun generateMemoryLevel(levelNumber: Int): MemoryLevel {
//    val numCards = minOf(2 + levelNumber / 3, 6)
//    val possibleOps = when {
//        levelNumber < 5  -> listOf(Op.ADD)
//        levelNumber < 10 -> listOf(Op.ADD, Op.SUB)
//        levelNumber < 15 -> listOf(Op.ADD, Op.SUB, Op.MUL)
//        else             -> Op.entries
//    }
//    val cards = List(numCards) {
//        val op = possibleOps.random()
//        val value = when (op) {
//            Op.MUL, Op.DIV -> (2..4).random()
//            else -> (1..9).random()
//        }
//        MemoryCard(op, value)
//    }
//    val start = (0..10).random()
//    return MemoryLevel(levelNumber, cards, start)
//}

//fun generateMemoryLevel(levelNumber: Int): MemoryLevel {
//    val numCards = minOf(2 + levelNumber / 3, 6)
//    val possibleOps = when {
//        levelNumber < 5  -> listOf(Op.ADD)
//        levelNumber < 10 -> listOf(Op.ADD, Op.SUB)
//        levelNumber < 15 -> listOf(Op.ADD, Op.SUB, Op.MUL)
//        else             -> Op.entries.toList()
//    }
//
//    val cards = List(numCards) {
//        val op = possibleOps.random()
//        val value = when (op) {
//            Op.MUL, Op.DIV -> (2..4).random()
//            else -> (1..9).random()
//        }
//        MemoryCard(op, value)
//    }
//    val start = (0..10).random()
//    Log.d("MathMemoryDebug", "Generated level $levelNumber: start=$start, cards=${cards.map { "${it.op}:${it.value}" }}")
//    return MemoryLevel(levelNumber, cards, start)
//}

fun generateMemoryLevel(levelNumber: Int): MemoryLevel {
    val numCards = minOf(2 + levelNumber / 3, 6)
    val possibleOps = when {
        levelNumber < 5  -> listOf(Op.ADD)
        levelNumber < 10 -> listOf(Op.ADD, Op.SUB)
        levelNumber < 15 -> listOf(Op.ADD, Op.SUB, Op.MUL)
        else             -> Op.entries.toList()
    }

    val cards = mutableListOf<MemoryCard>()
    var currentValue = (0..10).random()  // start value
    val start = currentValue

    repeat(numCards) {
        val op = possibleOps.random()
        val value = when (op) {
            Op.DIV -> {
                // For division, pick a divisor that divides currentValue exactly
                val divisors = (2..4).filter { currentValue % it == 0 }
                if (divisors.isEmpty()) {
                    // No exact divisor, fallback to a default divisor (avoid division)
                    1
                } else {
                    divisors.random()
                }
            }
            Op.MUL -> (2..4).random()
            else -> (1..9).random()
        }

        cards.add(MemoryCard(op, value))

        // Update currentValue to represent intermediate answer
        currentValue = when (op) {
            Op.ADD -> currentValue + value
            Op.SUB -> currentValue - value
            Op.MUL -> currentValue * value
            Op.DIV -> if (value != 0) currentValue / value else currentValue
        }

        // Optional: clamp currentValue to keep numbers manageable (help with final answer size)
        currentValue = currentValue.coerceIn(-100, 100)
    }

    return MemoryLevel(levelNumber, cards, start)
}


