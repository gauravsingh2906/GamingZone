package com.google.codelab.gamingzone.data.local1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.google.codelab.gamingzone.data.local1.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.TotalStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {

    // --- user identity ---
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getAnyUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    // --- total stats ---
    @Query("SELECT * FROM total_stats WHERE userId = :userId LIMIT 1")
    suspend fun getTotalStatsOnce(userId: String): TotalStatsEntity?

    @Query("SELECT * FROM total_stats WHERE userId = :userId LIMIT 1")
    fun observeTotalStats(userId: String): Flow<TotalStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotalStats(stats: TotalStatsEntity)

    // --- per-game stats ---
    @Query("SELECT * FROM per_game_stats WHERE userId = :userId AND gameName = :gameName LIMIT 1")
    suspend fun getPerGameStatsOnce(userId: String, gameName: String): PerGameStatsEntity?

    @Query("SELECT * FROM per_game_stats WHERE userId = :userId")
    fun observePerGameStats(userId: String): Flow<List<PerGameStatsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerGameStats(stats: PerGameStatsEntity)

    // Transactional update of both total and per-game stats
    @Transaction
    suspend fun updateStatsTransaction(userId: String, gameName: String, isWin: Boolean, isDraw: Boolean, xpEarned: Int) {
        // total
        val currentTotal = getTotalStatsOnce(userId) ?: TotalStatsEntity(userId = userId)
        val newTotal = currentTotal.copy(
            totalGamesPlayed = currentTotal.totalGamesPlayed + 1,
            totalWins = currentTotal.totalWins + if (isWin) 1 else 0,
            totalLosses = currentTotal.totalLosses + if (!isWin && !isDraw) 1 else 0,
            totalDraws = currentTotal.totalDraws + if (isDraw) 1 else 0,
            totalXP = currentTotal.totalXP + xpEarned
        )
        insertTotalStats(newTotal)

        // per-game
        val currentPerGame = getPerGameStatsOnce(userId, gameName)
        if (currentPerGame == null) {
            val created = PerGameStatsEntity(
                userId = userId,
                gameName = gameName,
                gamesPlayed = 1,
                wins = if (isWin) 1 else 0,
                losses = if (!isWin && !isDraw) 1 else 0,
                draws = if (isDraw) 1 else 0,
                xp = xpEarned
            )
            insertPerGameStats(created)
        } else {
            val updated = currentPerGame.copy(
                gamesPlayed = currentPerGame.gamesPlayed + 1,
                wins = currentPerGame.wins + if (isWin) 1 else 0,
                losses = currentPerGame.losses + if (!isWin && !isDraw) 1 else 0,
                draws = currentPerGame.draws + if (isDraw) 1 else 0,
                xp = currentPerGame.xp + xpEarned
            )
            // REPLACE by primary key (id) because insertPerGameStats uses REPLACE
            insertPerGameStats(updated)
        }
    }
}
