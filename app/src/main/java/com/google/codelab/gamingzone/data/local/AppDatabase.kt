package com.google.codelab.gamingzone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.google.codelab.gamingzone.data.local1.dao.AdvancedStatsDao
import com.google.codelab.gamingzone.data.local1.dao.LevelProgressDao
import com.google.codelab.gamingzone.data.local1.dao.UserStatsDao
import com.google.codelab.gamingzone.data.local1.entity.AdvancedStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.LevelProgressEntity
import com.google.codelab.gamingzone.data.local1.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.TotalStatsEntity

import com.google.codelab.gamingzone.data.local1.entity.UserEntity
import com.google.codelab.gamingzone.data.local2.dao.DailyMissionDao
import com.google.codelab.gamingzone.data.local2.entity.DailyMissionEntity

@Database(
    entities = [UserEntity::class, TotalStatsEntity::class, PerGameStatsEntity::class, AdvancedStatsEntity::class, LevelProgressEntity::class, DailyMissionEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userStatsDao(): UserStatsDao

    abstract fun advancedStatsDao(): AdvancedStatsDao

    abstract fun levelProgressDao(): LevelProgressDao

    abstract fun dailyMissionDao(): DailyMissionDao
}