package com.google.codelab.gamingzone.data.local2.repository

import com.google.codelab.gamingzone.data.local1.entity.TotalStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.UserEntity
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity
import java.util.UUID

interface StatsRepository {


    suspend fun updateGameResult(
        userId: String,
        gameName: String,
        levelReached: Int,
        won: Boolean,
        xpGained: Int,
        currentStreak: Int,
        bestStreak: Int,
        hintsUsed: Int,
        timeSpentSeconds: Long
    )

    suspend fun initUserIfNeeded(username: String?="Player"): String

    suspend fun updateUsername(userId: String, newUsername: String)
    suspend fun updateAvatar(userId: String, newAvatarUri: Int)
    suspend fun updateCoins(userId: String, newCoins: Int)

    suspend fun getProfile(userId: String): OverallProfileEntity?
    suspend fun getPerGameStats(userId: String, gameName: String): PerGameStatsEntity?
}
