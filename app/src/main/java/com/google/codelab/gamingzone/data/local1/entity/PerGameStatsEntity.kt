package com.google.codelab.gamingzone.data.local1.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "per_game_stats")
data class PerGameStatsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val gameName: String,
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val xp: Int = 0
)