package com.google.codelab.gamingzone.data.local2.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.codelab.gamingzone.data.local2.entity.DailyMissionEntity

interface DailyMissionRepository {
    suspend fun getMissionsForToday(userId:String): List<DailyMissionEntity>
 //   suspend fun updateMissionProgress(gameName: String, minutesPlayed: Int)

    suspend fun updateMissionProgress(userId: String, gameName: String, missionType: String, incrementBy: Int)
}

