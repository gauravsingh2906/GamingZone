package com.google.codelab.gamingzone.data.local2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.codelab.gamingzone.data.local2.entity.DailyMissionEntity

@Dao
interface DailyMissionDao {

    @Query("SELECT * FROM daily_missions WHERE date = :date")
    suspend fun getMissionsForDate(date: String): List<DailyMissionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<DailyMissionEntity>)

    @Update
    suspend fun updateMission(mission: DailyMissionEntity)
}
