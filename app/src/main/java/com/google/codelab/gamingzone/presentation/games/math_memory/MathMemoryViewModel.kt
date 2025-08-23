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
import com.google.codelab.gamingzone.data.local2.entity.PerGameStatsEntity
import com.google.codelab.gamingzone.data.local2.repository.StatsRepository
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.Default
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject
import kotlin.math.abs


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@HiltViewModel
class MathMemoryViewModel @Inject constructor(
    private val levelManager: LevelManager,
    private val repo: MathMemoryRepository,
    private val statsRepo: StatsRepository,
) : ViewModel() {

    private var hasSavedLevelForCurrentScreen = false

    private val _uiState = mutableStateOf<MathMemoryGameUIState?>(null)
    val uiState: State<MathMemoryGameUIState?> = _uiState

    var selectedTheme by mutableStateOf(Default.first())
        private set
    var unlockedThemes by mutableStateOf(setOf(Default.first().name))
        private set

    private val _answerOptions = mutableStateOf<List<AnswerOption>>(emptyList())
    val answerOptions: State<List<AnswerOption>> = _answerOptions

    var showUnlockAnimation by mutableStateOf(false)
        private set

    var newlyUnlockedThemeName by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            delay(3000)
            val userId = statsRepo.initUserIfNeeded()
            val profile = statsRepo.getProfile(userId)
            val savedLevel = profile?.mathMemoryCurrentLevel ?: 1
            Log.d("LevelLoad", "Loaded saved level: $savedLevel for user: $userId")
            levelManager.setLevel(savedLevel)
            setNewLevel(levelManager.currentLevel())
            hasSavedLevelForCurrentScreen = false
            loadThemes()
        }
    }

    fun loadCurrentLevel(userId: String) {
        viewModelScope.launch {
            val profile = statsRepo.getProfile(userId)
            val resumeLevel = profile?.mathMemoryCurrentLevel ?: 1
            Log.d("LevelLoad", "Loaded saved level: $resumeLevel for user: $userId")

            levelManager.setLevel(resumeLevel)
            val currentLevel = levelManager.currentLevel()

            setNewLevel(currentLevel)
            hasSavedLevelForCurrentScreen = false
        }
    }

    private fun setNewLevel(level: MemoryLevel) {
        Log.d("LevelState", "Setting new level: ${level.number}")
        _uiState.value = MathMemoryGameUIState(game = MathMemoryGameState(level))
        _answerOptions.value = generateAnswerOptions(level.correctAnswer)
    }

    fun saveCurrentLevel(userId: String, level: Int) {
        viewModelScope.launch {
            Log.d("LevelSave", "Saving level $level for user $userId")
            val profile = statsRepo.getProfile(userId)
            if (profile != null) {
                statsRepo.updateMathMemoryCurrentLevel(userId, level)
            }
        }
    }

    fun onAction(action: MathMemoryAction) {
        when (action) {
            is MathMemoryAction.RevealCards -> {
                _uiState.value = _uiState.value?.copy(
                    game = _uiState.value!!.game.copy(isShowCards = false)
                )
            }
            is MathMemoryAction.HideCards -> {
                _uiState.value = _uiState.value?.copy(
                    game = _uiState.value!!.game.copy(isShowCards = true)
                )
            }
            is MathMemoryAction.InputChanged -> {
                _uiState.value = _uiState.value?.copy(
                    game = _uiState.value!!.game.copy(userInput = action.value)
                )
            }
            is MathMemoryAction.SubmitAnswer -> {
                val isCorrect = _uiState.value?.game?.userInput?.toIntOrNull() == _uiState.value?.game?.level?.correctAnswer
                Log.d("Result", "Submit answer: $isCorrect, input: ${_uiState.value?.game?.userInput}")
                _uiState.value = _uiState.value?.copy(
                    game = _uiState.value!!.game.copy(showResult = true, isCorrect = isCorrect)
                )
            }
            is MathMemoryAction.NextLevel -> {
                Log.d("LevelFlow", "Moving to next level")
                hasSavedLevelForCurrentScreen = false
                levelManager.nextLevel()
                setNewLevel(levelManager.currentLevel())
                handleThemeUnlock(levelManager.currentLevel().number)
            }
            is MathMemoryAction.ResetGame -> {
                Log.d("LevelFlow", "Retry current level")
                hasSavedLevelForCurrentScreen = false
                setNewLevel(levelManager.currentLevel())
                // Reset input and UI flags
                _uiState.value = _uiState.value?.copy(
                    game = _uiState.value!!.game.copy(
                        userInput = "",
                        showResult = false,
                        isCorrect = false,
                        isShowCards = false
                    )
                )
            }
            is MathMemoryAction.SelectTheme -> {
                if (action.theme.name in _uiState.value?.theme?.unlockedThemes.orEmpty()) {
                    _uiState.value = _uiState.value?.copy(
                        theme = _uiState.value!!.theme.copy(
                            selectedTheme = action.theme,
                        )
                    )
                    selectedTheme = action.theme
                    viewModelScope.launch {
                        repo.setSelectedTheme(action.theme.name)
                    }
                }
            }
            is MathMemoryAction.UnlockNextTheme -> {
                val currentUnlocked = _uiState.value?.theme?.unlockedThemes.orEmpty()
                val nextTheme = Default.firstOrNull { it.name !in currentUnlocked }
                if (nextTheme != null) {
                    _uiState.value = _uiState.value?.copy(
                        theme = _uiState.value!!.theme.copy(
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

    fun onLevelResultAndSaveStats(
        userId: String,
        isCorrect: Boolean,
        hintsUsed: Int,
        timeSpentSeconds: Long
    ) = viewModelScope.launch {
        if (hasSavedLevelForCurrentScreen) {
            Log.d("LevelSave", "Already saved for this screen, skipping")
            return@launch
        }
        hasSavedLevelForCurrentScreen = true

        val levelNum = _uiState.value?.game?.level?.number ?: 1
        val nextLevel = if (isCorrect) levelNum + 1 else levelNum

        saveCurrentLevel(userId, nextLevel)

        val xpEarned = getXpForLevel(levelNum)
        statsRepo.updateGameResult(
            userId = userId,
            gameName = "math_memory",
            levelReached = levelNum,
            won = isCorrect,
            xpGained = xpEarned,
            hintsUsed = hintsUsed,
            timeSpentSeconds = timeSpentSeconds
        )

        // Update levelManager and UI accordingly
        levelManager.setLevel(nextLevel)
        setNewLevel(levelManager.currentLevel())
    }

    private fun generateAnswerOptions(correct: Int): List<AnswerOption> {
        val options = mutableSetOf(correct)
        val rand = Random()
        val maxOffset = maxOf(3, abs(correct) / 3)
        var attempts = 0

        while (options.size < 4 && attempts < 25) {
            val offset = rand.nextInt(maxOffset) + 1
            val candidate = if (rand.nextBoolean()) correct + offset else correct - offset
            if (candidate != correct) {
                options.add(candidate)
            }
            attempts++
        }

        var fallbackValue = if (correct > 4) correct - 4 else 1
        while (options.size < 4) {
            if (fallbackValue != correct && fallbackValue !in options) {
                options.add(fallbackValue)
            }
            fallbackValue++
        }

        return options.shuffled().map { AnswerOption(it, it == correct) }
    }

    private fun handleThemeUnlock(levelNumber: Int) {
        val unlockNewTheme = levelNumber % 5 == 0
        if (unlockNewTheme) {
            val currentThemeNames = _uiState.value?.theme?.unlockedThemes.orEmpty()
            val nextTheme = Default.firstOrNull { it.name !in currentThemeNames }
            if (nextTheme != null) {
                onThemeUnlocked(nextTheme.name)
                _uiState.value = _uiState.value?.copy(
                    theme = _uiState.value!!.theme.copy(
                        unlockedThemes = currentThemeNames + nextTheme.name,
                        selectedTheme = nextTheme
                    )
                )
                selectedTheme = nextTheme
                unlockedThemes = currentThemeNames + nextTheme.name
            }
        }
    }

    fun onThemeUnlocked(themeName: String) {
        Log.d("ThemeUnlock", "Unlocked theme: $themeName")
        newlyUnlockedThemeName = themeName
        showUnlockAnimation = true
    }

    fun onUnlockAnimationDismiss() {
        showUnlockAnimation = false
    }

    fun loadThemes() {
        viewModelScope.launch {
            val unlocked = repo.getUnlockedThemes()
            val selectedThemeName = repo.getSelectedTheme()
            val selected = Default.find { it.name == selectedThemeName } ?: Default.first()
            _uiState.value = _uiState.value?.copy(
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

    fun getXpForLevel(level: Int): Int {
        return 10 + ((level - 1) / 5) * 2
    }
}



