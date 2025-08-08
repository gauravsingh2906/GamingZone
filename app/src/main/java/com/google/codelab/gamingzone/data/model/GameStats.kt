package com.google.codelab.gamingzone.data.model

data class GameStats(
    val gameName: String,
    val gamesPlayed: Int=0,
    val wins: Int=0,
    val losses: Int=0,
    val totalDraws:Int=0,
    val xpEarned:Int=0,
    val easyGamesPlayed: Int=0,
    val mediumGamesPlayed: Int=0,
    val hardGamesPlayed: Int=0
)