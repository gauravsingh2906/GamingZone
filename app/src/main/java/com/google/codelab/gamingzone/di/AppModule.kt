package com.google.codelab.gamingzone.di

import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import com.google.codelab.gamingzone.MyApplication
import com.google.codelab.gamingzone.data.LogicPuzzleRepository
import com.google.codelab.gamingzone.domain.math_dungeon.MathDungeonRepository
import com.google.codelab.gamingzone.domain.math_dungeon.MathDungeonRepositoryImpl
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



    @Binds
    @Singleton
    abstract fun mathPuzzleGenerator(
        default: MathDungeonRepositoryImpl
    ): MathDungeonRepository



}
