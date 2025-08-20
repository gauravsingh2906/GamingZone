package com.google.codelab.gamingzone.data.local2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity

@Dao
interface PerGameStatsDao {
    @Query("SELECT * FROM per_game_statistics WHERE userId = :userId AND gameName = :gameName LIMIT 1")
    suspend fun getStatsForGame(userId: String, gameName: String): PerGameStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: PerGameStatsEntity)
}


