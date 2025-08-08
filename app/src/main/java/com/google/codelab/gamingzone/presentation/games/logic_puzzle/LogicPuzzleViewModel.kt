package com.google.codelab.gamingzone.presentation.games.logic_puzzle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.LogicPuzzleRepository
import com.google.codelab.gamingzone.domain.logic_story.LogicPuzzle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogicPuzzleViewModel @Inject constructor(private val repository: LogicPuzzleRepository) : ViewModel() {

    private val _puzzles = MutableStateFlow<List<LogicPuzzle>>(emptyList())
    val puzzles: StateFlow<List<LogicPuzzle>> = _puzzles

    private val _currentIndex = MutableStateFlow(0)
    val currentPuzzle: StateFlow<LogicPuzzle?> = _currentIndex
        .combine(_puzzles) { index, list ->
            list.getOrNull(index)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _selectedName = MutableStateFlow<String?>(null)
    val selectedName = _selectedName.asStateFlow()

    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
    val isAnswerCorrect = _isAnswerCorrect.asStateFlow()

    private val _revealTruth = MutableStateFlow(false)
    val revealTruth = _revealTruth.asStateFlow()

    private val _uiEvents = Channel<UIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    sealed class UIEvent {
        object PlayRevealSoundAndVibrate : UIEvent()
    }
    fun selectCharacter(name: String) {
        _selectedName.value = name
    }



    fun checkAnswer() {
        val puzzle = currentPuzzle.value ?: return
        val selected = selectedName.value ?: return

        val selectedChar = puzzle.characters.find { it.name == selected }
        _isAnswerCorrect.value = selectedChar?.truth == true
    }

    fun revealTruth() {
        _revealTruth.value = true

        viewModelScope.launch {
            _uiEvents.send(UIEvent.PlayRevealSoundAndVibrate)
        }
    }

    fun resetAnswer() {
        _isAnswerCorrect.value = null
        _revealTruth.value = false
        _selectedName.value = null
    }


    fun loadPuzzles() {
        viewModelScope.launch {
            val loaded = repository.loadAllPuzzles().shuffled()
            _puzzles.value = loaded
            _currentIndex.value=0
        }
    }

    fun nextPuzzle() {
        _currentIndex.update { it + 1 }
    }

    fun prevPuzzle() {
        _currentIndex.update { (it - 1).coerceAtLeast(0) }
    }

    fun canGoBack(): Boolean = _currentIndex.value > 0
    fun canGoForward(): Boolean = _currentIndex.value < (_puzzles.value.size - 1)



}