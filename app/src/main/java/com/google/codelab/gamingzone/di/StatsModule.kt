package com.google.codelab.gamingzone.di

import android.content.Context
import androidx.room.Room
import com.google.codelab.gamingzone.data.local2.StatsDatabase
import com.google.codelab.gamingzone.data.local2.dao.OverallProfileDao
import com.google.codelab.gamingzone.data.local2.dao.PerGameStatsDao
import com.google.codelab.gamingzone.data.local2.repository.StatsRepository
import com.google.codelab.gamingzone.data.local2.repository.StatsRepositoryImpl
import com.google.codelab.gamingzone.presentation.games.math_memory.MathMemoryStatsRepository
import com.google.codelab.gamingzone.presentation.games.math_memory.MathMemoryStatsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatsModule {

    @Provides
    @Singleton
    fun provideStatsDatabase(@ApplicationContext appContext: Context): StatsDatabase =
        Room.databaseBuilder(appContext, StatsDatabase::class.java, "stats_db").build()

    @Provides
    fun providePerGameStatsDao(db: StatsDatabase): PerGameStatsDao = db.perGameStatsDao()

    @Provides
    fun provideOverallProfileDao(db: StatsDatabase): OverallProfileDao = db.overallProfileDao()

    @Provides
    @Singleton
    fun provideStatsRepository(
        perGameStatsDao: PerGameStatsDao,
        overallProfileDao: OverallProfileDao
    ): StatsRepository = StatsRepositoryImpl(perGameStatsDao, overallProfileDao)


    @Provides
    @Singleton
    fun provideMathMemoryStatsRepository(
        perGameStatsDao: PerGameStatsDao,
        statsRepository: StatsRepository
    ): MathMemoryStatsRepository =
        MathMemoryStatsRepositoryImpl(perGameStatsDao, statsRepository)


}
