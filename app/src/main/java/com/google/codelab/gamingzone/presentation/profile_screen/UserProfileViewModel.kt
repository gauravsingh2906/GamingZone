package com.google.codelab.gamingzone.presentation.profile_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.codelab.gamingzone.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity
import com.google.codelab.gamingzone.data.model.GameResult

import com.google.codelab.gamingzone.data.model.GameStats
import com.google.codelab.gamingzone.domain.repository.GameResultRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

//@HiltViewModel
//class UserProfileViewModel @Inject constructor(
//    private val repository: UserProfileRepository,
////    private val gameResultRepository: GameResultRepository
//) : ViewModel() {
//
//    private val _profileState = mutableStateOf<UserProfileEntity?>(null)
//    val profileState: State<UserProfileEntity?> = _profileState
//
//    private val _gameStats = mutableStateOf<List<GameStats>>(emptyList())
//    val gameStats: State<List<GameStats>> = _gameStats
//
//    private val _winPercentage = mutableStateOf(0f)
//    val winPercentage: State<Float> = _winPercentage
//
//
//    val adjectives = listOf("Cool", "Silent", "Funky", "Smart", "Dark", "Fire")
//    val nouns = listOf("Ninja", "Cat", "Wizard", "Dragon", "Knight", "Fox")
//    val number = (100..999).random()
//
//    val username = "${adjectives.random()}${nouns.random()}_$number"
//
//    var defaultAvatarId = R.drawable.ic_star1
//    val defaultUnlockedAvatars = listOf(defaultAvatarId)
//
//
//
//    init {
//        viewModelScope.launch {
//            val profile = repository.getProfile()
//            if (profile == null) {
//                val newProfile = UserProfileEntity(
//                    username = username,
//                    avatarId = defaultAvatarId,
//                    unlockedAvatars = defaultUnlockedAvatars,
//                )
//                repository.saveProfile(newProfile)
//                _profileState.value = newProfile
//            } else {
//                _profileState.value = profile
//              //  loadStats(profile.id)
//            }
//        }
//    }
//
////    private suspend fun loadStats(userId: String) {
////        val totalGames = gameResultRepository.getTotalGamesPlayed(userId)
////        val totalWins = gameResultRepository.getTotalWins(userId)
////
////        _winPercentage.value = if (totalGames > 0) {
////            totalWins.toFloat() / totalGames * 100
////        } else 0f
////
////        _gameStats.value = gameResultRepository.getGameStatsGrouped(userId)
////    }
//
//
//
//    fun saveProfile(username: String,avatarId:Int,unlockedAvatars:List<Int>) {
//        viewModelScope.launch {
//            val userId = _profileState.value?.id?: UUID.randomUUID().toString()
//            val updated = UserProfileEntity(
//                id = userId,
//                username = username,
//                avatarId = avatarId,
//                unlockedAvatars = unlockedAvatars,
//            )
//            repository.saveProfile(updated)
//            _profileState.value = updated
//        }
//    }
//
//    fun updateAfterGame(score: Int, minutesPlayed: Int) {
//        viewModelScope.launch {
//            _profileState.value?.let { current ->
//                val updatedXP = current.currentXP + score
//                val updatedGamesPlayed = current.gamesPlayed + 1
//                val updatedPlayTime = current.totalPlayTimeInMinutes + minutesPlayed
//                val newHighestScore = maxOf(current.highestScore, score)
//                Log.d("All","Score + ${current.highestScore} + $score")
//
//                val newLevel = calculateLevel(updatedXP)
//                val newXpToNext = calculateXpToNextLevel(newLevel)
//
//                val updated = current.copy(
//                    level = newLevel, // it;s updating
//                    currentXP = updatedXP, // it's updating
//                    xpToNextLevel = newXpToNext,
//                    gameStats = current.gameStats,
//                    gamesPlayed = updatedGamesPlayed, //it's updating
//                    highestScore = newHighestScore,  //
//                    totalPlayTimeInMinutes = updatedPlayTime, //
//                )
//                Log.d("All","ViewModel $updated")
//              //  val profile = repository.updateProfile(updated)
//              //  Log.d("All","Profile $profile")
//                _profileState.value = updated
//                repository.updateProfile(updated)
//                Log.d("All","ViewModel ${_profileState.value}")
//            }
//        }
//    }
//
//    fun recordGameResult(gameName: String, result: GameResult) {
//        viewModelScope.launch {
//            updateGameStats(gameName, result)
//            _profileState.value = repository.getProfile() // refresh UI
//        }
//    }
//
//    suspend fun updateGameStats(
//        gameName: String,
//        result: GameResult
//    ) {
//        val profile = repository.getProfile() ?: return
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
//        repository.updateProfile(profile.copy(gameStats = updatedStats))
//    }
//
//
//
//    private fun calculateLevel(xp: Int): Int = (xp / 100) + 1
//    private fun calculateXpToNextLevel(level: Int): Int = level * 100
//
//    suspend fun updateUserAfterGame(xpEarned: Int) {
//        val currentProfile = repository.getProfile()
//    }
//
//
//
//}