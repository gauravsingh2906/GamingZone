package com.google.codelab.gamingzone.data.local2

import androidx.room.Database
import androidx.room.RoomDatabase
import com.google.codelab.gamingzone.data.local2.dao.OverallProfileDao
import com.google.codelab.gamingzone.data.local2.dao.PerGameStatsDao
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity

@Database(
    entities = [PerGameStatsEntity::class, OverallProfileEntity::class],
    version = 3)
abstract class StatsDatabase : RoomDatabase() {

    abstract fun perGameStatsDao(): PerGameStatsDao

    abstract fun overallProfileDao(): OverallProfileDao

}
