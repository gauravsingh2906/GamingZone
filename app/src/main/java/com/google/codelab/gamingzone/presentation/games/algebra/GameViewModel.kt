package com.google.codelab.gamingzone.presentation.games.algebra


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val manager = GameManager()

    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level

    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver

    private val _timeRemaining = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining

//    private val _timeLeft = mutableStateOf(config.timeLimitSeconds())
//    val timeLeft: State<Int> = _timeLeft

    private var questionStartTime: Long = 0L
    private var timerJob: Job? = null

    fun startGame() {
        _score.value = 0
        _level.value = 1
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
            if (_timeRemaining.value <= 0 && !_gameOver.value) {
                // Time is up
                endGame()
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
                gameRepository.recordResult(q.gameType, correct, false, xp, timeSec)
            }

            if ( (_score.value/100) >_level.value) {
                _level.value =_level.value+1
            }
            startNext()
        } else {
            // if not correct do the game over
            viewModelScope.launch {
                gameRepository.recordResult(q.gameType,
                    correct = false,
                    isDraw = false,
                    xpEarned = xp,
                    timeTakenSec = timeSec
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

    private fun checkNumericAnswer(userAnswer: Any?, correctAnswer: Number): Boolean {
        return when (correctAnswer) {
            is Int -> (userAnswer as? Int) == correctAnswer
            is Double -> {
                val tolerance = 0.01
                val ans = (userAnswer as? String)?.toDoubleOrNull() ?: (userAnswer as? Double)
                ans != null && kotlin.math.abs(ans - correctAnswer) < tolerance
            }
            else -> false
        }
    }

    fun setLevel(l: Int) { _level.value = l.coerceAtLeast(1) }
}
