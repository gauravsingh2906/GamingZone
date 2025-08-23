package com.google.codelab.gamingzone.presentation.daily_missions

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.local2.entity.DailyMissionEntity
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity
import com.google.codelab.gamingzone.data.local2.repository.DailyMissionRepository
import com.google.codelab.gamingzone.data.local2.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class DailyMissionViewModel @Inject constructor(
    private val repository: DailyMissionRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()


    private val _missions = MutableStateFlow<List<DailyMissionEntity>>(emptyList())
    val missions: StateFlow<List<DailyMissionEntity>> = _missions

//    private val _profile = MutableStateFlow<OverallProfileEntity?>(null)
//    val profile: StateFlow<OverallProfileEntity?> = _profile



//    init {
//        // create user row if needed and set _userId
//        viewModelScope.launch {
//            val id = statsRepository.initUserIfNeeded()
//            Log.d("Id- mission",id)
//            _userId.value = id
//            loadMissions(id)
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleMidnightRefresh() {
        val now = LocalDateTime.now()
        val midnight = now.toLocalDate().plusDays(1).atStartOfDay()
        val delayMillis = java.time.Duration.between(now, midnight).toMillis()

        viewModelScope.launch {
            delay(delayMillis)
            // Reload missions after midnight
            loadMissions(userId.value ?: return@launch)
            // Optionally, schedule again for next day
            scheduleMidnightRefresh()
        }
    }




    private fun loadMissions(userId: String) {
        viewModelScope.launch {
            _missions.value = repository.getMissionsForToday(userId)
        }
    }

    fun updateProgress(gameName: String,missionType:String, minutes: Int) {
        viewModelScope.launch {
            repository.updateMissionProgress(
                userId = _userId.value ?: return@launch ,
                gameName = gameName,
                missionType = missionType,
                incrementBy = minutes
            )
            loadMissions(_userId.value ?: return@launch)
        }
    }

}
