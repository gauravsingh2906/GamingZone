package com.google.codelab.gamingzone.di

import android.content.Context
import androidx.room.Room
import com.google.codelab.gamingzone.data.SudokuDatabase
import com.google.codelab.gamingzone.presentation.games.math_memory.LevelManager
import com.google.codelab.gamingzone.presentation.games.math_memory.MathMemoryRepository
import com.google.codelab.gamingzone.presentation.games.math_memory.MathMemoryRepositoryImpl
import com.google.codelab.gamingzone.presentation.games.math_memory.generateMemoryLevel
import com.google.codelab.gamingzone.presentation.games.sudoku_screen.SudokuResultDao
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.Default
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameModule {
    @Provides
    fun provideLevelManager(): LevelManager =
        LevelManager(::generateMemoryLevel)
}

@Module
@InstallIn(SingletonComponent::class)
object MathMemoryModule {
    @Provides
    fun provideRepository(
        @ApplicationContext context: Context
    ): MathMemoryRepository = MathMemoryRepositoryImpl(context, Default)
}

@Module
@InstallIn(SingletonComponent::class)
object SudokuDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SudokuDatabase {
        return Room.databaseBuilder(
            context,
            SudokuDatabase::class.java,
            "sudoku_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSudokuResultDao(database: SudokuDatabase): SudokuResultDao {
        return database.sudokuResultDao()
    }
}




