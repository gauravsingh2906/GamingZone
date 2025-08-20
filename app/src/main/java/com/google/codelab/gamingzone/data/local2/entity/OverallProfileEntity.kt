package com.google.codelab.gamingzone.data.local2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "overall_profile")
data class OverallProfileEntity(
    @PrimaryKey var userId: String,
    val username: String = "Player",
    val avatarUri: String? = null,          // URI string/path to avatar image
    val coins: Int = 0,                     // coins to spend on avatar/name customizations
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val totalDraws: Int = 0,
    val totalXP: Int = 0,
    val overallHighestLevel: Int = 0,
    val bestStreak: Int = 0,
    val totalHintsUsed: Int = 0,
    val totalTimeSeconds: Long = 0L
)