package com.google.codelab.gamingzone.data.local2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_missions")
data class DailyMissionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // e.g., "2025-08-21"
    val gameName: String, // "Sudoku", "Algebra", or "Overall"
    val requiredMinutes: Int,
    val progressMinutes: Int = 0,
    val isCompleted: Boolean = false
)

