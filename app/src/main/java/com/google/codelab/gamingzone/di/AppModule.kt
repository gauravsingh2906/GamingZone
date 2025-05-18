package com.google.codelab.gamingzone.di

import com.google.codelab.gamingzone.domain.sudoku.DefaultPuzzleGenerator
import com.google.codelab.gamingzone.domain.sudoku.PuzzleGenerator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindPuzzleGenerator(
        default: DefaultPuzzleGenerator
    ): PuzzleGenerator
}
