package com.google.codelab.gamingzone.data.repository

import com.google.codelab.gamingzone.data.local1.dao.AdvancedStatsDao
import com.google.codelab.gamingzone.data.local1.entity.AdvancedStatsEntity
import com.google.codelab.gamingzone.presentation.games.algebra.GameType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val statsRepository: StatsRepository, // existing repo
    private val advancedStatsDao: AdvancedStatsDao
) {

     /**
     * Ensure user exists and then record:
     * - central stats via StatsRepository.recordGameResult(...)
     * - advanced per-type stats via AdvancedStatsDao.updateSummary / insert
     */
    suspend fun recordResult(
        gameType: GameType,
        correct: Boolean,
        isDraw: Boolean,
        xpEarned: Int,
        timeTakenSec: Long
    ) {
        // get or create user
        val userId = statsRepository.initUserIfNeeded()

        // central stats (your existing totals table)
        statsRepository.recordGameResult(userId, gameType.name, correct, isDraw, xpEarned)

        // update advanced stats
        withContext(Dispatchers.IO) {
            val existing = advancedStatsDao.getFor(userId, gameType.name)
            if (existing == null) {
                val entity = AdvancedStatsEntity(
                    userId = userId,
                    gameType = gameType.name,
                    totalPlayed = 1,
                    totalCorrect = if (correct) 1 else 0,
                    totalWrong = if (!correct) 1 else 0,
                    bestStreak = if (correct) 1 else 0,
                    lastStreak = if (correct) 1 else 0,
                    totalTimeSeconds = timeTakenSec,
                    xpEarned = xpEarned
                )
                advancedStatsDao.insert(entity)
            } else {
                val newLastStreak = if (correct) existing.lastStreak + 1 else 0
                // updateSummary will update bestStreak based on lastStreak per DAO SQL
                advancedStatsDao.updateSummary(
                    userId = userId,
                    gameType = gameType.name,
                    played = 1,
                    correct = if (correct) 1 else 0,
                    wrong = if (!correct) 1 else 0,
                    lastStreak = newLastStreak,
                    timeSec = timeTakenSec,
                    xp = xpEarned
                )
            }
        }
    }
}