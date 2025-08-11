package com.google.codelab.gamingzone.di


import android.content.Context
import androidx.room.Room
import com.google.codelab.gamingzone.data.local.AppDatabase

import com.google.codelab.gamingzone.data.local1.dao.UserStatsDao
import com.google.codelab.gamingzone.data.repository.StatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "game_stats_db"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserStatsDao(db: AppDatabase): UserStatsDao = db.userStatsDao()

    @Provides
    @Singleton
    fun provideStatsRepository(dao: UserStatsDao): StatsRepository = StatsRepository(dao)
}
