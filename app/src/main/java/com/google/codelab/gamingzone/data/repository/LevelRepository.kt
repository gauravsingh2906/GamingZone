package com.google.codelab.gamingzone.data.repository

import android.util.Log
import com.google.codelab.gamingzone.data.local1.dao.LevelProgressDao
import com.google.codelab.gamingzone.data.local1.entity.LevelProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LevelRepository @Inject constructor(
    private val dao: LevelProgressDao
) {
   // private val gameId = "algebra" // you can make this dynamic if needed

    fun getMaxUnlockedLevel(gameId: String): Flow<Int> =
        dao.getMaxUnlockedLevel(gameId).map { it ?: 1 }

    suspend fun unlockNextLevelIfNeeded(currentLevel: Int,gameId: String) {
        val maxLevel = dao.getMaxUnlockedLevelOnce(gameId) ?: 1
        if (currentLevel >= maxLevel) {
            dao.updateMaxUnlockedLevel(gameId, currentLevel + 1)
        }
    }

    suspend fun ensureInitialized(gameId: String) {
        val existing = dao.getMaxUnlockedLevelOnce(gameId)
        if (existing == null) {
            dao.insert(LevelProgressEntity(gameId = gameId, maxUnlockedLevel = 1))
        }
    }
}

