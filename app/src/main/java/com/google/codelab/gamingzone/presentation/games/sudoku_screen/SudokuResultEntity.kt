package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sudoku_results")
data class SudokuResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val difficulty: String,
    val puzzle: String,         // Original puzzle grid
    val userSolution: String,      // User's final solution
    val hintsUsed: Int,         // Total hints used
    val mistakesMade: Int,      // Total mistakes made
    val timeTakenMinutes: Int,  // Time in minutes
    val timestamp: Long = System.currentTimeMillis()       // When the game was completed
)

