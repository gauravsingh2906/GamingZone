package com.google.codelab.gamingzone.domain.repository

import com.google.codelab.gamingzone.data.local.dao.UserProfileDao
import com.google.codelab.gamingzone.data.local.entity.GameResultEntity
import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity
import javax.inject.Inject



class GameResultRepository @Inject constructor(
    private val userDao: UserProfileDao
) {

    suspend fun updateStatsAfterGame(
        userId: String,
        gameType: String, // e.g., "Sudoku"
        difficulty: String, // "Easy", "Medium", "Hard"
        isWin: Boolean
    ) {
        val users = userDao.getUserProfile() ?: return



        // Increase total games played
        val newTotalGames = users.gamesPlayed + 1

        // Increase XP based on difficulty and win/loss
        val xpGained = calculateXp(difficulty, isWin)
        val newXp = users.currentXP + xpGained

        // Determine new level and XP needed
        val (newLevel, newXpToNextLevel) = calculateLevelAndRemainingXp(newXp)

        // Create a mutable map of game stats
        val updatedGameStats = users.gameStats.toMutableMap()

        val keyBase = "$gameType-${difficulty.lowercase()}"

        // Update wins/losses
        val winKey = "$keyBase-wins"
        val lossKey = "$keyBase-losses"

//        updatedGameStats[winKey] = (updatedGameStats[winKey] ?: 0) + if (isWin) 1 else 0
//        updatedGameStats[lossKey] = (updatedGameStats[lossKey] ?: 0) + if (!isWin) 1 else 0

        val updatedUser = users.copy(
            gamesPlayed = newTotalGames,
            currentXP = newXp,
            level = newLevel,
            xpToNextLevel = newXpToNextLevel,
            gameStats = updatedGameStats
        )

        userDao.updateProfile(updatedUser)
    }

    private fun calculateXp(difficulty: String, isWin: Boolean): Int {
        return when (difficulty) {
            "Easy" -> if (isWin) 20 else 10
            "Medium" -> if (isWin) 40 else 20
            "Hard" -> if (isWin) 60 else 30
            else -> 0
        }
    }

    private fun calculateLevelAndRemainingXp(totalXp: Int): Pair<Int, Int> {
        val xpPerLevel = 100
        val level = totalXp / xpPerLevel + 1
        val xpToNextLevel = xpPerLevel - (totalXp % xpPerLevel)
        return level to xpToNextLevel
    }
}


