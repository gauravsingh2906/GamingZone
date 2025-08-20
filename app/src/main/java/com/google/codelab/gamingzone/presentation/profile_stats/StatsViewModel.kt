package com.google.codelab.gamingzone.presentation.profile_stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local2.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsRepo: StatsRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _profile = MutableStateFlow<OverallProfileEntity?>(null)
    val profile: StateFlow<OverallProfileEntity?> = _profile

    private val _perGameStats = MutableStateFlow<List<PerGameStatsEntity>>(emptyList())
    val perGameStats: StateFlow<List<PerGameStatsEntity>> = _perGameStats

    //    init {
//        loadProfile(userId = "c97f320d-4681-4e07-aeca-f305ea33d7e9")
//    }'
    init {
        // create user row if needed and set _userId
        viewModelScope.launch {
            val id = statsRepo.initUserIfNeeded()
            _userId.value = id
            loadProfile(id)
        }
    }

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _profile.value = statsRepo.getProfile(userId)
            Log.d("User",_profile.value.toString())
            _perGameStats.value = listOfNotNull(
                statsRepo.getPerGameStats(userId, "sudoku"),
                statsRepo.getPerGameStats(userId, "math_memory"),
                statsRepo.getPerGameStats(userId,"algebra")

            )
        }
    }

    fun updateGameAndProfile(
        userId: String, gameName: String, level: Int, won: Boolean, xp: Int,
        streak: Int, bestStreak: Int, hints: Int, timeSec: Long
    ) = viewModelScope.launch {
        statsRepo.updateGameResult(
            userId = userId, gameName, level, won, xp, streak, bestStreak, hints, timeSec
        )
        loadProfile(userId)
    }

    fun changeUsername(userId: String, username: String) = viewModelScope.launch {
        statsRepo.updateUsername(userId, username)
    }

    fun changeAvatar(userId: String, avatarUri: String) = viewModelScope.launch {
        statsRepo.updateAvatar(userId, avatarUri)
    }
}
