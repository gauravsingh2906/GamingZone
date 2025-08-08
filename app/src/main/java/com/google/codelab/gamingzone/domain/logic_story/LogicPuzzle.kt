package com.google.codelab.gamingzone.domain.logic_story


data class LogicPuzzle(
    val level: Int,
    val context: String,
    val characters: List<CharacterStatement>
)

data class CharacterStatement(
    val name: String,
    val statement: String,
    val truth: Boolean
)

