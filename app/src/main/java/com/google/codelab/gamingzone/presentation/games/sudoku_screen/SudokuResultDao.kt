package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import androidx.room.*

@Dao
interface SudokuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: SudokuResultEntity)

    @Query("SELECT * FROM sudoku_results ORDER BY timestamp DESC")
    suspend fun getAllResults(): List<SudokuResultEntity>

    @Delete
    suspend fun deleteResult(result: SudokuResultEntity)
}
