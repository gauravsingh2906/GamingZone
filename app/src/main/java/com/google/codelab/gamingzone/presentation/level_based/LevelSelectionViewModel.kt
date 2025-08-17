package com.google.codelab.gamingzone.presentation.level_based

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.repository.LevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LevelSelectionViewModel @Inject constructor(
    private val repository: LevelRepository
) : ViewModel() {

    val maxUnlockedLevel = repository.getMaxUnlockedLevel()
        .map { level -> if (level < 1) 1 else level }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1
        ) // default 1

}