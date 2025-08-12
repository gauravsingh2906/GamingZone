package com.google.codelab.gamingzone.data.repository

import com.google.codelab.gamingzone.data.local1.dao.AdvancedStatsDao
import com.google.codelab.gamingzone.data.local1.entity.AdvancedStatsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdvancedStatsRepository @Inject constructor(
    private val dao: AdvancedStatsDao
) {

    fun observeAllForUser(userId: String) = dao.observeAllForUser(userId)

    suspend fun getFor(userId: String, gameType: String): AdvancedStatsEntity? = withContext(
        Dispatchers.IO) {
        dao.getFor(userId, gameType)
    }

    suspend fun insert(entity: AdvancedStatsEntity) = withContext(Dispatchers.IO) {
        dao.insert(entity)
    }

    suspend fun updateSummary(userId: String, gameType: String, played: Int, correct: Int, wrong: Int, lastStreak: Int, timeSec: Long, xp: Int) =
        withContext(Dispatchers.IO) {
            dao.updateSummary(userId, gameType, played, correct, wrong, lastStreak, timeSec, xp)
        }
}