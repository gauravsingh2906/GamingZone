package com.google.codelab.gamingzone.data.local2.entity

import androidx.room.Entity

@Entity(
    tableName = "per_game_statistics",
    primaryKeys = ["userId", "gameName"]
)
data class PerGameStatsEntity(
    val userId: String,
    val gameName: String,  // e.g., "sudoku", "math_memory"
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val xp: Int = 0,
    val highestLevel: Int = 0,
    val totalHintsUsed: Int = 0,
    val totalTimeSeconds: Long = 0L
)
