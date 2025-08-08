package com.google.codelab.gamingzone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.codelab.gamingzone.data.local.dao.GameResultDao
import com.google.codelab.gamingzone.data.local.dao.UserProfileDao
import com.google.codelab.gamingzone.data.local.entity.GameResultEntity
import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity
import com.google.codelab.gamingzone.data.mapper.Converters

@Database(entities = [UserProfileEntity::class, GameResultEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun userProfileDao(): UserProfileDao

    abstract fun gameResultDao(): GameResultDao

}