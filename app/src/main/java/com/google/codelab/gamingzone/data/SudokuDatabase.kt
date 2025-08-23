package com.google.codelab.gamingzone.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuResultDao
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuResultEntity

@Database(entities = [SudokuResultEntity::class], version = 2)
abstract class SudokuDatabase : RoomDatabase() {

    abstract fun sudokuResultDao(): SudokuResultDao


}
