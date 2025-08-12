package com.google.codelab.gamingzone.data.local1.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advanced_stats")
data class AdvancedStatsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val gameType: String,        // e.g., GameType.MISSING_NUMBER.name
    val totalPlayed: Int = 0,
    val totalCorrect: Int = 0,
    val totalWrong: Int = 0,
    val bestStreak: Int = 0,
    val lastStreak: Int = 0,
    val totalTimeSeconds: Long = 0L,
    val xpEarned: Int = 0
)