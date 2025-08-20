package com.google.codelab.gamingzone.presentation.games.math_memory

import android.util.Log
import com.google.codelab.gamingzone.data.local2.dao.PerGameStatsDao
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local2.repository.StatsRepository
import javax.inject.Inject

class MathMemoryStatsRepositoryImpl @Inject constructor(
    private val perGameStatsDao: PerGameStatsDao,
    private val statsRepository: StatsRepository // <-- your overall/profile repository
) : MathMemoryStatsRepository {

    private val GAME_NAME = "math_memory"

    override suspend fun recordMathResult(
        userId: String,
        level: Int,
        isWin: Boolean,
        xp: Int,
        streak: Int,
        bestStreak: Int,
        hintsUsed: Int,
        timeSpentSec: Long
    ) {
        // Update this game's per-game stats row
        val old = perGameStatsDao.getStatsForGame(userId, GAME_NAME)
        Log.d("MathStats", "Old stats for $GAME_NAME: $old")
        val updated = if (old == null) {
            Log.d("MathStats", "First play, creating new stats row.")
            PerGameStatsEntity(
                userId = userId,
                gameName = GAME_NAME,
                gamesPlayed = 1,
                wins = if (isWin) 1 else 0,
                losses = if (!isWin) 1 else 0,
                xp = xp,
                highestLevel = level,
                bestStreak = bestStreak,
                currentStreak = streak,
                totalHintsUsed = hintsUsed,
                totalTimeSeconds = timeSpentSec
            )
        } else {
            Log.d("MathStats", "Second play, creating old stats.")
            old.copy(
                gamesPlayed = old.gamesPlayed + 1,
                wins = old.wins + if (isWin) 1 else 0,
                losses = old.losses + if (!isWin) 1 else 0,
                xp = old.xp + xp,
                highestLevel = maxOf(old.highestLevel, level),
                bestStreak = maxOf(old.bestStreak, bestStreak),
                currentStreak = streak,
                totalHintsUsed = old.totalHintsUsed + hintsUsed,
                totalTimeSeconds = old.totalTimeSeconds + timeSpentSec
            )
        }
        perGameStatsDao.insertStats(updated)

        // Then update the total/overall stats as well
        statsRepository.updateGameResult(
            userId = userId,
            gameName = GAME_NAME,
            levelReached = level,
            won = isWin,
            xpGained = xp,
            currentStreak = streak,
            bestStreak = bestStreak,
            hintsUsed = hintsUsed,
            timeSpentSeconds = timeSpentSec
        )
    }

    override suspend fun getStats(userId: String): PerGameStatsEntity? {
        return perGameStatsDao.getStatsForGame(userId, GAME_NAME)
    }
}
