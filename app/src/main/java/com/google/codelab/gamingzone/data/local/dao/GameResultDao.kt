package com.google.codelab.gamingzone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.codelab.gamingzone.data.local.entity.GameResultEntity

import com.google.codelab.gamingzone.data.model.GameStats

@Dao
interface GameResultDao {

    @Query("SELECT COUNT(*) FROM game_results WHERE userId = :userId AND isWin = 1")
    suspend fun getTotalWins(userId: String): Int

    @Query("SELECT COUNT(*) FROM game_results WHERE userId = :userId AND isWin = 0")
    suspend fun getTotalLosses(userId: String): Int

    @Query("SELECT COUNT(*) FROM game_results WHERE userId = :userId")
    suspend fun getTotalGamesPlayed(userId: String): Int

  //  @Query("SELECT gameName, difficulty, COUNT(*) as timesPlayed, SUM(CASE WHEN isWin = 1 THEN 1 ELSE 0 END) as wins FROM game_results WHERE userId = :userId GROUP BY gameName, difficulty")
//    suspend fun getGameStatsGrouped(userId: String): List<GameStats>
}



