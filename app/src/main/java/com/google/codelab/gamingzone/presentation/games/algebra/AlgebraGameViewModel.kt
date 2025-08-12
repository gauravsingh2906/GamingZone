package com.google.codelab.gamingzone.presentation.games.algebra

import androidx.lifecycle.ViewModel
import com.google.codelab.gamingzone.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel
//class AlgebraGameViewModel @Inject constructor(
//    private val statsRepository: StatsRepository,
//    private val advancedStatsRepository: AdvancedStatsRepository
//) : ViewModel() {
//
//    private val _currentQuestion = mutableStateOf(generateQuestion())
//    val currentQuestion: State<AlgebraQuestion> = _currentQuestion
//
//    private val _score = mutableStateOf(0)
//    val score: State<Int> = _score
//
//    private var startTime = System.currentTimeMillis()
//
//    fun submitAnswer(answer: Int) {
//        val question = _currentQuestion.value
//        val isCorrect = question.correctAnswer == answer
//
//        if (isCorrect) {
//            _score.value += 10
//        }
//
//        viewModelScope.launch {
//            // Save general game stats
//            statsRepository.recordGameResult(
//                gameId = "algebra_game",
//                result = if (isCorrect) "win" else "loss",
//                xpEarned = if (isCorrect) 5 else 1
//            )
//
//            // Save advanced stats
//            advancedStatsRepository.recordAdvancedGameStats(
//                gameId = "algebra_game",
//                level = question.level,
//                timeTakenMs = System.currentTimeMillis() - startTime,
//                accuracy = if (isCorrect) 100 else 0
//            )
//        }
//
//        _currentQuestion.value = generateQuestion()
//        startTime = System.currentTimeMillis()
//    }
//
//    private fun generateQuestion(): AlgebraQuestion {
//        val x = (1..10).random()
//        val total = (x + (1..5).random())
//        val missing = total - x
//        return AlgebraQuestion(
//            equation = "x + $missing = $total",
//            correctAnswer = x,
//            level = (1..5).random()
//        )
//    }
//}
//
//data class AlgebraQuestion(
//    val equation: String,
//    val correctAnswer: Int,
//    val level: Int
//)
