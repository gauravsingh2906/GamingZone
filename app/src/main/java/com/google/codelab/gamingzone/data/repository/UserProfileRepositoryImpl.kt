package com.google.codelab.gamingzone.data.repository


//class UserProfileRepositoryImpl @Inject constructor(
//    private val dao: UserProfileDao
//): UserProfileRepository {
//    override suspend fun getProfile(): UserProfileEntity? {
//        return dao.getUserProfile()
//    }
//
//    override suspend fun saveProfile(profile: UserProfileEntity) {
//        return dao.insertProfile(profile)
//    }
//
//    override suspend fun updateProfile(profile: UserProfileEntity) {
//        dao.updateProfile(profile)
//    }
//
//    override suspend fun updateGameStats(
//        gameName: String,
//        result: GameResult
//    ) {
//        val profile = dao.getUserProfile() ?: return
//        Log.d("All", "Profile $profile")
//        val updatedStats = profile.gameStats.toMutableMap()
//        Log.d("All", "UpdatedStats $updatedStats")
//        val currentStats = updatedStats[gameName] ?: GameStats()
//        Log.d("All", "CURRENT STATS $currentStats")
//
//        updatedStats[gameName] = currentStats.copy(
//            gamesPlayed = currentStats.gamesPlayed + 1,
//            wins = currentStats.wins + if (result.isWin == true) 1 else 0,
//            losses = currentStats.losses + if (result.isWin == true) 0 else 1
//        )
//
//        val total = updatedStats[gameName]
//        Log.d("All", "Game STATS $total")
//
//
//        dao.updateProfile(profile.copy(gameStats = updatedStats))
//    }
//
//    suspend fun updateUserAfterGame(xpEarned: Int,gameDurationMinutes:Int,score: Int) {
//        val currentProfile = dao.getUserProfile()
//
//        val newXP = currentProfile?.currentXP?.plus(xpEarned)
//
//        val newGamesPlayed = currentProfile?.gamesPlayed?.plus(1)
//
//        val newTotalPlayTime = currentProfile?.totalPlayTimeInMinutes?.plus(gameDurationMinutes)
//
//        val newHighestScore = maxOf(currentProfile?.highestScore ?: 0 ,score)
//
//        val leveledUp = newXP!! >= currentProfile.xpToNextLevel
//
//      //  val updatedXP = if (leveledUp) newXP-currentProfile.level +1
//
//    }
//
//
//}