package com.google.codelab.gamingzone.presentation.games.algebra


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.codelab.gamingzone.data.local2.repository.StatsRepository
import com.google.codelab.gamingzone.data.repository.GameRepository
import com.google.codelab.gamingzone.data.repository.LevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val levelRepository: LevelRepository,
    private val statsRepository: StatsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

  //  val userId = savedStateHandle.toRoute<>()

    private val manager = GameManager()

    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level

    private val _question = MutableStateFlow<Question?>(manager.nextQuestion(1))
    val question: StateFlow<Question?> = _question

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver

    private val _levelCompleted = MutableStateFlow(false)
    val levelCompleted: StateFlow<Boolean> = _levelCompleted

    private val _timeRemaining = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining

//    private val _timeLeft = mutableStateOf(config.timeLimitSeconds())
//    val timeLeft: State<Int> = _timeLeft

    private var questionStartTime: Long = 0L
    private var timerJob: Job? = null

    private val _maxUnlockedLevel = MutableStateFlow(1)
    val maxUnlockedLevel: StateFlow<Int> = _maxUnlockedLevel

    private var currentLevel = 1

    fun setLevel(level: Int) {
        currentLevel = level
        Log.d("Level", "Level set to $level")
        Log.d("Level Current", "Level set to $currentLevel")
        _level.value = level
        Log.d("Level Value","Level value:${_level.value}")
    }

    fun levelCompleted() {
        viewModelScope.launch {

            val currentMax = levelRepository.getMaxUnlockedLevel().first() // get latest value from flow
            Log.d("Level", "Current max: $currentMax")
            Log.d("Level", "Current level: $currentLevel")

            if (currentLevel >= currentMax) {
                levelRepository.unlockNextLevelIfNeeded(currentLevel)
            }

        }
    }


    //happen on click
    fun markLevelCompleted() {
        _levelCompleted.value = true
        unlockNextLevelIfNeeded()
    }

    // mark on complete
    private fun unlockNextLevelIfNeeded() {
        viewModelScope.launch {
            val nextLevel = currentLevel + 1
            levelRepository.unlockNextLevelIfNeeded(currentLevel)
            _maxUnlockedLevel.value = nextLevel  // update local cache
            Log.d("Level", "Unlocked next level: $nextLevel")
        }
    }

    init {
        viewModelScope.launch {
            levelRepository.ensureInitialized()
            levelRepository.getMaxUnlockedLevel().collect { level ->
                _maxUnlockedLevel.value = level
                Log.d("Level", "Collected max unlocked level = $level")
            }
        }
    }




//    init {
//        viewModelScope.launch {
//            gameRepository.getMaxUnlockedLevel().collect { level ->
//                Log.d("Level",level.toString())
//                _maxUnlockedLevel.value = level
//            }
//        }
//    }

    //happen on click
//    fun completeLevel(currentLevel: Int) {
//        if (currentLevel >= _maxUnlockedLevel.value) {
//            val newLevel = currentLevel + 1
//            _maxUnlockedLevel.value = newLevel
//            viewModelScope.launch {
//                gameRepository.updateMaxUnlockedLevel(newLevel)
//            }
//        }
//    }



    fun startGame() {
        _score.value = 0
        _level.value = currentLevel
        _gameOver.value = false
        startNext()
    }


    fun startNext() {
        val q = manager.nextQuestion(_level.value)
        _question.value = q
        questionStartTime = System.currentTimeMillis()

        startTimer(LevelConfig(_level.value).timeLimitSeconds())
    }

    fun startTimer(seconds: Int) {
        timerJob?.cancel()
        _timeRemaining.value = seconds
        timerJob = viewModelScope.launch {
            while (_timeRemaining.value > 0 && !_gameOver.value) {
                delay(1000)
                _timeRemaining.value--
            }
            if (_timeRemaining.value <= 0 || !_gameOver.value) {
                // Time is up
               // endGame()
            }
        }
    }

    private fun onTimeOver() {
        // Handle time over - game over or next question
    }




    fun submitAnswer(userAnswer: Any?) {
        if (_gameOver.value) return

        val q = _question.value ?: return
        val timeSec = (System.currentTimeMillis() - questionStartTime) / 1000L

        val correct = when (q) {
            is Question.MissingNumber -> (userAnswer as? Int) == q.answer
            is Question.MissingOperator -> (userAnswer as? Char) == q.answer
            is Question.TrueFalse -> (userAnswer as? Boolean) == q.isCorrect
            is Question.Reverse -> (userAnswer as? Char) == q.answer
            is Question.Mix -> {
                val inner = q.inner
                when (inner) {
                    is Question.MissingNumber -> (userAnswer as? Int) == inner.answer
                    is Question.MissingOperator -> (userAnswer as? Char) == inner.answer
                    is Question.TrueFalse -> (userAnswer as? Boolean) == inner.isCorrect
                    is Question.Reverse -> (userAnswer as? Char) == inner.answer
                    else -> false
                }
            }
        }

        // XP calculation
        val baseXp = q.difficultyLevel * 5
        val bonus = if (correct) (LevelConfig(q.difficultyLevel).xpForCorrectBase() / 2) else 0
        val xp = baseXp + bonus



        // persist asynchronously


        if (correct) {
            _score.value += xp
            viewModelScope.launch {
             //   gameRepository.recordResult(q.gameType, true, false, xp, timeSec)

                statsRepository.updateGameResult(
                    userId = statsRepository.initUserIfNeeded(),
                    gameName = "algebra",
                    levelReached = currentLevel,
                    won = true,
                    xpGained = xp,
                    currentStreak =10 ,
                    bestStreak = 4,
                    hintsUsed =3 ,
                    timeSpentSeconds = timeSec
                )




      //          levelRepository.unlockNextLevelIfNeeded(currentLevel+1)
            }

            if ((_score.value/100) > _level.value) {
                markLevelCompleted()
             //   completeLevel(_level.value+1)
              //  _level.value =_level.value+1
            }
            startNext()
        } else {
            // if not correct do the game over
            viewModelScope.launch {
//                gameRepository.recordResult(q.gameType,
//                    correct = false,
//                    isDraw = false,
//                    xpEarned = xp,
//                    timeTakenSec = timeSec
//                )
                statsRepository.updateGameResult(
                    userId = statsRepository.initUserIfNeeded(),
                    gameName = "algebra",
                    levelReached = currentLevel,
                    won = false,
                    xpGained = xp,
                    currentStreak =10 ,
                    bestStreak = 4,
                    hintsUsed =3 ,
                    timeSpentSeconds = timeSec
                )
            }
            endGame()
        }
        // simple level up rule
//        if (correct && (_score.value / 100) > _level.value) {
//            _level.value = _level.value + 1
//            startNext()
//        } else {
//            startNext()
//        }

        // next question

    }

    private fun endGame() {
        timerJob?.cancel()
        _gameOver.value = true
    }

}
