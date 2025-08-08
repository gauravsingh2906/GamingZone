package com.google.codelab.gamingzone.presentation.games.math_memory

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.Default
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject



@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@HiltViewModel
class MathMemoryViewModel @Inject constructor(
    private val levelManager: LevelManager,
    private val repo: MathMemoryRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(
        MathMemoryGameUIState(game = MathMemoryGameState(levelManager.currentLevel()))
    )
    val uiState: State<MathMemoryGameUIState> = _uiState

    var selectedTheme by mutableStateOf(Default.first())
        private set
    var unlockedThemes by mutableStateOf(setOf(Default.first().name))
        private set

    private val _answerOptions = mutableStateOf<List<AnswerOption>>(emptyList())
    val answerOptions: State<List<AnswerOption>> = _answerOptions

    var showUnlockAnimation by mutableStateOf(false)
        private set

    // Track which theme was just unlocked (for display)
    var newlyUnlockedThemeName by mutableStateOf<String?>(null)
        private set


    init {
        setNewLevel(levelManager.currentLevel())
     //   loadThemes()
      //  updateAnswerOptions()
    }


    private fun setNewLevel(level: MemoryLevel) {
        _uiState.value = _uiState.value.copy(
            game = MathMemoryGameState(level)
        )
        _answerOptions.value = generateAnswerOptions(level.correctAnswer)
    }


    private fun updateAnswerOptions() {
        val state = _uiState.value.game.level.correctAnswer
        Log.d("State",state.toString())
        val correctAnswer = levelManager.currentLevel().correctAnswer
        Log.d("Correct",correctAnswer.toString())
        _answerOptions.value = generateAnswerOptions(state)
    }

    private fun generateAnswerOptions(correct: Int): List<AnswerOption> {
        val options = mutableSetOf(correct)
        Log.d("options",options.toString())
        val rand = java.util.Random()
        val maxOffset = maxOf(3, correct / 3)

        while (options.size < 4) {
            val offset = rand.nextInt(maxOffset) + 1 // 1 ~ maxOffset
            Log.d("Offset",offset.toString())
            val candidate = if (rand.nextBoolean()) correct + offset else correct - offset
            Log.d("Candidate",candidate.toString())
            if (candidate > 0) options.add(candidate)
        }
        val value = options.shuffled().map { AnswerOption(it, it == correct) }
        Log.d("Value",value.toString())
        return value
    }

    fun onAction(action: MathMemoryAction) {
        when (action) {
            is MathMemoryAction.RevealCards -> {
                _uiState.value = _uiState.value.copy(
                    game = _uiState.value.game.copy(isShowCards = false)
                )
            }

            is MathMemoryAction.InputChanged -> {
                // Not strictly necessary now, but keep to sync input and actions
                _uiState.value = _uiState.value.copy(
                    game = _uiState.value.game.copy(userInput = action.value)
                )
            }

            is MathMemoryAction.SubmitAnswer -> {
                val isCorrect = _uiState.value.game.userInput.toIntOrNull() == _uiState.value.game.level.correctAnswer
                _uiState.value = _uiState.value.copy(
                    game = _uiState.value.game.copy(showResult = true, isCorrect = isCorrect)
                )
            }

            is MathMemoryAction.NextLevel -> {
                levelManager.nextLevel()
                val newLevel = levelManager.currentLevel()
                setNewLevel(newLevel)
                Log.d("Theme",newLevel.number.toString())
             //   checkAndUnlockTheme(newLevel.number)
           //     updateAnswerOptions()
                val unlockNewTheme = (levelManager.currentLevel().number % 5 == 0)
                Log.d("Theme",unlockNewTheme.toString())
                val currentThemeNames = _uiState.value.theme.unlockedThemes

                val newThemeState = if (unlockNewTheme) {
                    // Find next theme in builtInThemes not yet unlocked by name
                    val nextTheme = Default.firstOrNull { it.name !in currentThemeNames }
                    if (nextTheme != null) {
                        _uiState.value.theme.copy(
                            unlockedThemes = currentThemeNames + nextTheme.name,
                            selectedTheme = nextTheme
                        )
                    } else {
                        _uiState.value.theme
                    }
                } else {
                    _uiState.value.theme
                }

                _uiState.value = MathMemoryGameUIState(
                    game = MathMemoryGameState(levelManager.currentLevel()),
                    theme = newThemeState
                )
                selectedTheme = newThemeState.selectedTheme
                unlockedThemes = newThemeState.unlockedThemes
            }

            is MathMemoryAction.ResetGame -> {
                levelManager.reset()
                setNewLevel(levelManager.currentLevel())
             //   updateAnswerOptions()
                _uiState.value = MathMemoryGameUIState(
                    game = MathMemoryGameState(levelManager.currentLevel()),
                    theme = ThemeState() // Reset theme to default
                )
                selectedTheme = Default.first()
                unlockedThemes = setOf(Default.first().name)

            }

            is MathMemoryAction.SelectTheme -> {
                if (action.theme.name in _uiState.value.theme.unlockedThemes) {
                    _uiState.value = _uiState.value.copy(
                        theme = _uiState.value.theme.copy(selectedTheme = action.theme)
                    )
                    selectedTheme = action.theme
                    viewModelScope.launch {
                        repo.setSelectedTheme(action.theme.name)
                    }
                }
            }

            is MathMemoryAction.UnlockNextTheme -> {
                val currentUnlocked = _uiState.value.theme.unlockedThemes
                val nextTheme = Default.firstOrNull { it.name !in currentUnlocked }
                if (nextTheme != null) {
                    _uiState.value = _uiState.value.copy(
                        theme = _uiState.value.theme.copy(
                            unlockedThemes = currentUnlocked + nextTheme.name
                        )
                    )
                    unlockedThemes = currentUnlocked + nextTheme.name

                    viewModelScope.launch {
                        repo.addUnlockedTheme(nextTheme.name)
                    }
                }
            }
        }
    }

    // Call this after unlocking a new theme (in your unlock logic)
    fun onThemeUnlocked(themeName: String) {
        Log.d("Theme",themeName+"changed")
        newlyUnlockedThemeName = themeName
        showUnlockAnimation = true
    }

    // Call this when user dismisses the animation overlay
    fun onUnlockAnimationDismiss() {
        showUnlockAnimation = false
    }


    //this theme is called every time a level is advanced
    private fun checkAndUnlockTheme(currentLevelNumber: Int) {
        // Choose a theme to unlock only if it's new
        val themeName = getThemeNameForLevel(currentLevelNumber)
        if (themeName != null && themeName !in unlockedThemes) {
            onThemeUnlocked(themeName)
            // Also add to unlockedThemes permanently (in memory and in repo/DataStore)
        }
    }
    private fun getThemeNameForLevel(level: Int): String {
        // Your mapping: e.g., "Galaxy" at level 10, etc
        return when (level) {
            2 -> "Galaxy"
            10 -> "Neon"
            // ...etc
            else -> "Special"
        }
    }


    fun loadThemes() {
        viewModelScope.launch {
            val unlocked = repo.getUnlockedThemes()
            val selectedThemeName = repo.getSelectedTheme()
            val selected = Default.find { it.name == selectedThemeName } ?: Default.first()
            _uiState.value = _uiState.value.copy(
                theme = ThemeState(
                    unlockedThemes = unlocked,
                    selectedTheme = selected
                )
            )
        }
    }

    fun startMemorizationTimer(delayMs: Long) {
        viewModelScope.launch {
            delay(delayMs)
            onAction(MathMemoryAction.RevealCards)
        }
    }
}
