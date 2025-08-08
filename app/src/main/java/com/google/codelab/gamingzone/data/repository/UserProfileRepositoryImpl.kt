package com.google.codelab.gamingzone.data.repository

import com.google.codelab.gamingzone.data.local.dao.UserProfileDao
import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity
import com.google.codelab.gamingzone.domain.repository.UserProfileRepository
import javax.inject.Inject


class UserProfileRepositoryImpl @Inject constructor(
    private val dao: UserProfileDao
): UserProfileRepository {
    override suspend fun getProfile(): UserProfileEntity? {
        return dao.getUserProfile()
    }

    override suspend fun saveProfile(profile: UserProfileEntity) {
        return dao.insertProfile(profile)
    }

    override suspend fun updateProfile(profile: UserProfileEntity) {
        dao.updateProfile(profile)
    }

    suspend fun updateUserAfterGame(xpEarned: Int,gameDurationMinutes:Int,score: Int) {
        val currentProfile = dao.getUserProfile()

        val newXP = currentProfile?.currentXP?.plus(xpEarned)

        val newGamesPlayed = currentProfile?.gamesPlayed?.plus(1)

        val newTotalPlayTime = currentProfile?.totalPlayTimeInMinutes?.plus(gameDurationMinutes)

        val newHighestScore = maxOf(currentProfile?.highestScore ?: 0 ,score)

        val leveledUp = newXP!! >= currentProfile.xpToNextLevel

      //  val updatedXP = if (leveledUp) newXP-currentProfile.level +1

    }


}