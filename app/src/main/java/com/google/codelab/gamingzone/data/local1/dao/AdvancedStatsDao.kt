package com.google.codelab.gamingzone.data.local1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.codelab.gamingzone.data.local1.entity.AdvancedStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdvancedStatsDao {

    @Query("SELECT * FROM advanced_stats WHERE userId = :userId AND gameType = :gameType LIMIT 1")
    suspend fun getFor(userId: String, gameType: String): AdvancedStatsEntity?

    @Query("SELECT * FROM advanced_stats WHERE userId = :userId")
    fun observeAllForUser(userId: String): Flow<List<AdvancedStatsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AdvancedStatsEntity)

    // convenience update query for partial updates
    @Query("""
        UPDATE advanced_stats
        SET totalPlayed = totalPlayed + :played,
            totalCorrect = totalCorrect + :correct,
            totalWrong = totalWrong + :wrong,
            lastStreak = :lastStreak,
            bestStreak = CASE WHEN bestStreak < :lastStreak THEN :lastStreak ELSE bestStreak END,
            totalTimeSeconds = totalTimeSeconds + :timeSec,
            xpEarned = xpEarned + :xp
        WHERE userId = :userId AND gameType = :gameType
    """)
    suspend fun updateSummary(
        userId: String,
        gameType: String,
        played: Int,
        correct: Int,
        wrong: Int,
        lastStreak: Int,
        timeSec: Long,
        xp: Int
    )
}