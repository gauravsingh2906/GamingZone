package com.google.codelab.gamingzone.data.repository

import com.google.codelab.gamingzone.data.local1.dao.UserStatsDao
import com.google.codelab.gamingzone.data.local1.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.TotalStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val dao: UserStatsDao
) {

    // Ensure there is one user in DB and return its userId
    suspend fun initUserIfNeeded(username: String = "Player"): String {
        val existing = dao.getAnyUser()
        if (existing != null) return existing.userId

        val newId = UUID.randomUUID().toString()
        val user = UserEntity(userId = newId, username = username)
        dao.insertUser(user)
        // create default total stats row
        dao.insertTotalStats(TotalStatsEntity(userId = newId))
        return newId
    }

    fun observeTotalStats(userId: String): Flow<TotalStatsEntity?> = dao.observeTotalStats(userId)
    fun observePerGameStats(userId: String): Flow<List<PerGameStatsEntity>> = dao.observePerGameStats(userId)

    suspend fun recordGameResult(userId: String, gameName: String, isWin: Boolean, isDraw: Boolean, xpEarned: Int) {
        dao.updateStatsTransaction(userId, gameName, isWin, isDraw, xpEarned)
    }

    // helpful once-only getters
    suspend fun getTotalStatsOnce(userId: String): TotalStatsEntity? = dao.getTotalStatsOnce(userId)
    suspend fun getPerGameStatsOnce(userId: String, gameName: String): PerGameStatsEntity? = dao.getPerGameStatsOnce(userId, gameName)
}