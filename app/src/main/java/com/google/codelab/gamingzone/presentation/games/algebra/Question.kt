package com.google.codelab.gamingzone.presentation.games.algebra

sealed class Question {

    abstract val gameType: GameType
    abstract val difficultyLevel: Int

    data class MissingNumber(
        val left: Int,
        val right: Int,
        val operator: Char,
        val answer: Int,
        override val difficultyLevel: Int
    ) : Question() {
        override val gameType = GameType.MISSING_NUMBER
    }

    data class MissingOperator(
        val a: Int,
        val b: Int,
        val result: Int,
        val options: List<Char>,
        val answer: Char,
        override val difficultyLevel: Int
    ) : Question() {
        override val gameType = GameType.MISSING_OPERATOR
    }

    data class TrueFalse(
        val expression: String,
        val isCorrect: Boolean,
        override val difficultyLevel: Int
    ) : Question() {
        override val gameType = GameType.TRUE_FALSE
    }

    data class Reverse(
        val a: Int,
        val b: Int,
        val result: Int,
        val options: List<Char>,
        val answer: Char,
        override val difficultyLevel: Int
    ) : Question() {
        override val gameType = GameType.REVERSE
    }

    data class Mix(
        val inner: Question,
        override val difficultyLevel: Int
    ) : Question() {
        override val gameType = GameType.MIX
    }


}