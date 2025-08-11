package com.google.codelab.gamingzone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.google.codelab.gamingzone.data.local1.dao.UserStatsDao
import com.google.codelab.gamingzone.data.local1.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.TotalStatsEntity

import com.google.codelab.gamingzone.data.local1.entity.UserEntity

@Database(
    entities = [UserEntity::class, TotalStatsEntity::class, PerGameStatsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userStatsDao(): UserStatsDao
}