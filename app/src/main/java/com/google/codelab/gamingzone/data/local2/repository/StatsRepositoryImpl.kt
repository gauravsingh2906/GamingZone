package com.google.codelab.gamingzone.data.local2.repository

import android.util.Log
import com.google.codelab.gamingzone.R
import com.google.codelab.gamingzone.data.local1.entity.TotalStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.UserEntity
import com.google.codelab.gamingzone.data.local2.dao.OverallProfileDao
import com.google.codelab.gamingzone.data.local2.dao.PerGameStatsDao
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val perGameStatsDao: PerGameStatsDao,
    private val overallProfileDao: OverallProfileDao
) : StatsRepository {

    override suspend fun updateGameResult(
        userId: String,
        gameName: String,
        levelReached: Int,
        won: Boolean,
        xpGained: Int,
        hintsUsed: Int,
        timeSpentSeconds: Long
    ) {
      //  val userId = initUserIfNeeded()
        val perGame = perGameStatsDao.getStatsForGame(userId, gameName)

        Log.d("MathStats", "Old stats for $gameName: $perGame")
        val newPerGame = if (perGame == null) {
            Log.d("MathStats", "First play, creating new stats row.")
            PerGameStatsEntity(
                userId = userId,
                gameName = gameName,
                gamesPlayed = 1,
                wins = if (won) 1 else 0,
                losses = if (!won) 1 else 0,
                xp = xpGained,
                highestLevel = levelReached,
                totalHintsUsed = hintsUsed,
                totalTimeSeconds = timeSpentSeconds
            )
        } else {
            perGame.copy(
                gamesPlayed = perGame.gamesPlayed+1,
                wins = perGame.wins + if (won) 1 else 0,
                losses = perGame.losses + if (!won) 1 else 0,
                xp = perGame.xp + xpGained,
                highestLevel = maxOf(perGame.highestLevel, levelReached),
                totalHintsUsed = perGame.totalHintsUsed + hintsUsed,
                totalTimeSeconds = perGame.totalTimeSeconds + timeSpentSeconds
            )
        }
        perGameStatsDao.insertStats(newPerGame)

        val overall = overallProfileDao.getProfile(userId)
        val newOverall = if (overall == null) {
            Log.d("MathStats", "First play, creating new overall profile.")
            OverallProfileEntity(
                userId = userId,
                totalGamesPlayed = 1,
                totalWins = if (won) 1 else 0,
                totalLosses = if (!won) 1 else 0,
                totalXP = xpGained,
                overallHighestLevel = levelReached,
                totalHintsUsed = hintsUsed,
                totalTimeSeconds = timeSpentSeconds
            )
        } else {
            overall.copy(
                totalGamesPlayed = overall.totalGamesPlayed + 1,
                totalWins = overall.totalWins + if (won) 1 else 0,
                totalLosses = overall.totalLosses + if (!won) 1 else 0,
                totalXP = overall.totalXP + xpGained,
                overallHighestLevel = maxOf(overall.overallHighestLevel, levelReached),
                totalHintsUsed = overall.totalHintsUsed + hintsUsed,
                totalTimeSeconds = overall.totalTimeSeconds + timeSpentSeconds
            )
        }
        overallProfileDao.insertProfile(newOverall)
    }

    override suspend fun initUserIfNeeded(username: String?): String {
        val existing = overallProfileDao.getAnyUser()

        if (existing != null) return existing.userId


        val newId = UUID.randomUUID().toString()
        Log.d("Id", "First id generated $newId")

        val adjectives = listOf("Cool", "Silent", "Funky", "Smart", "Dark", "Fire")
        val nouns = listOf("Ninja", "Cat", "Wizard", "Dragon", "Knight", "Fox")
        val number = (100..999).random()

        val username = "${adjectives.random()}${nouns.random()}_$number"

        var defaultAvatarId = listOf<Int>(R.drawable.avatar_1,R.drawable.avatar_4,R.drawable.avatar_2,R.drawable.avatar_5,R.drawable.avatar_6,R.drawable.avatar_7)
        val defaultUnlockedAvatars = defaultAvatarId.random()


        val us = OverallProfileEntity(userId = newId, username = username, avatarUri = defaultUnlockedAvatars)
        val user = UserEntity(userId = newId, username = username)
        overallProfileDao.insertProfile(us)
        // create default total stats row
        overallProfileDao.updateProfile(us)
       // overallProfileDao.insertTotalStats(TotalStatsEntity(userId = newId))
        return newId
    }

    override suspend fun updateUsername(userId: String, newUsername: String) {
        val profile = overallProfileDao.getProfile(userId) ?: return
        overallProfileDao.updateProfile(profile.copy(username = newUsername))
    }

    override suspend fun updateAvatar(userId: String, newAvatarUri: Int) {
        val profile = overallProfileDao.getProfile(userId) ?: return
        overallProfileDao.updateProfile(profile.copy(avatarUri = newAvatarUri))
    }

    override suspend fun updateCoins(userId: String, newCoins: Int) {
        val profile = overallProfileDao.getProfile(userId) ?: return
        overallProfileDao.updateProfile(profile.copy(coins = newCoins))
    }

    override suspend fun getProfile(userId: String) = overallProfileDao.getProfile(userId)

    override suspend fun getPerGameStats(userId: String, gameName: String) =
        perGameStatsDao.getStatsForGame(userId, gameName)

    override suspend fun updateMathMemoryCurrentLevel(userId: String, level: Int) {
        val profile = overallProfileDao.getProfile(userId) ?: return
        overallProfileDao.updateProfile(profile.copy(mathMemoryCurrentLevel = level))
    }

}
