package com.google.codelab.gamingzone.presentation.games.algebra

import androidx.compose.runtime.LaunchedEffect
import kotlin.random.Random

class GameManager {

    fun nextQuestion(level: Int): Question {
        val config = LevelConfig(level.coerceAtLeast(1))
        val pick = Random.nextInt(100)
        val type = when {
            level <=5 -> if (pick<70) GameType.MISSING_NUMBER else GameType.TRUE_FALSE
            level <=10 -> when {
                pick <40 -> GameType.MISSING_NUMBER
                pick<70 -> GameType.MIX
                else -> GameType.MISSING_OPERATOR
            }
            level <=15 -> when {
                pick<30 -> GameType.MISSING_NUMBER
                pick<55 -> GameType.MISSING_OPERATOR
                pick<80 -> GameType.REVERSE
                else -> GameType.TRUE_FALSE
            }

            else -> GameType.MIX
        }

        return when(type) {
            GameType.MISSING_NUMBER -> genMissingNumber(config)
            GameType.MISSING_OPERATOR ->genMissingOperator(config)
            GameType.TRUE_FALSE -> genTrueFalse(config)
            GameType.REVERSE -> genReverse(config)
            GameType.MIX -> Question.Mix(
                nextQuestion((level-1).coerceAtLeast(1)), difficultyLevel = config.level
            )
        }


    }



}