package com.google.codelab.gamingzone.presentation.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.local1.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.TotalStatsEntity
import com.google.codelab.gamingzone.data.local1.entity.UserEntity
import com.google.codelab.gamingzone.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserStatsViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    // userId will be initialized in init block asynchronously
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    // total stats flow that depends on userId
    val totalStatsFlow: StateFlow<TotalStatsEntity?> =
        _userId.filterNotNull()
            .flatMapLatest { repository.observeTotalStats(it) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // per-game stats flow
    val perGameStatsFlow: StateFlow<List<PerGameStatsEntity>> =
        _userId.filterNotNull()
            .flatMapLatest { repository.observePerGameStats(it) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        // create user row if needed and set _userId
        viewModelScope.launch {
            val id = repository.initUserIfNeeded()
            _userId.value = id
        }
    }

    // Call to record a game result
    fun recordGameResult(gameName: String, isWin: Boolean, isDraw: Boolean = false, xpEarned: Int) {
        val id = _userId.value ?: return
        viewModelScope.launch {
            repository.recordGameResult(id, gameName, isWin, isDraw, xpEarned)
        }
    }
}
