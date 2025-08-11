package com.google.codelab.gamingzone.data.local1.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "total_stats")
data class TotalStatsEntity(
    @PrimaryKey val userId: String,
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val totalDraws: Int = 0,
    val totalXP: Int = 0
)