package com.google.codelab.gamingzone.presentation.games.math_memory

import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity

interface MathMemoryStatsRepository {
    suspend fun recordMathResult(
        userId: String,
        level: Int,
        isWin: Boolean,
        xp: Int,
        streak: Int,
        bestStreak: Int,
        hintsUsed: Int,
        timeSpentSec: Long
    )
    suspend fun getStats(userId: String): PerGameStatsEntity?
}
