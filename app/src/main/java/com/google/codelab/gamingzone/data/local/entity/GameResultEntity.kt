package com.google.codelab.gamingzone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val gameName: String,
    val difficulty: String, // easy, medium, hard
    val isWin: Boolean,
    val score: Int,
    val timePlayed: Int,
    val timestamp: Long = System.currentTimeMillis()
)



