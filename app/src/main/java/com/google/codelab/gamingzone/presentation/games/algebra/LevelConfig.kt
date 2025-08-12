package com.google.codelab.gamingzone.presentation.games.algebra

data class LevelConfig(val level: Int) {

    fun numberRange(): IntRange {
        return when {
            level <= 5 -> 0..10
            level <= 10 -> 0..20
            level <= 15 -> -20..30
            level <= 20 -> -50..50
            else -> -100..200
        }
    }

    fun allowedOperators(): List<Char> {
        return when {
            level <= 5 -> listOf('+', '-')
            level <= 10 -> listOf('+', '-', '*')
            level <= 15 -> listOf('+', '-', '*', '/')
            else -> listOf('+', '-', '*', '/')
        }
    }

    fun timeLimitSeconds(): Int {
        return when {
            level <= 5 -> 12
            level <= 10 -> 10
            level <= 15 -> 8
            level <= 20 -> 6
            else -> 5
        }
    }

    fun xpForCorrectBase(): Int {
        return when {
            level <= 5 -> 10
            level <= 10 -> 15
            level <= 15 -> 25
            level <= 20 -> 35
            else -> 50
        }
    }


}
