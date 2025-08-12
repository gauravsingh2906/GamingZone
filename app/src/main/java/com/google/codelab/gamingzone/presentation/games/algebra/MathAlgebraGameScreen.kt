package com.google.codelab.gamingzone.presentation.games.algebra

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.random.Random


@Composable
fun MathAlgebraGameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val question by viewModel.question.collectAsState()
    val score by viewModel.score.collectAsState()
    val level by viewModel.level.collectAsState()
    val timeLeft by viewModel.timeRemaining.collectAsState()
    val isGameOver by viewModel.gameOver.collectAsState()

    var textAnswer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

//    LaunchedEffect(Unit) {
//        viewModel.startTimer(LevelConfig(level).timeLimitSeconds())
//    }

    LaunchedEffect(Unit) {
        viewModel.startNext()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        if (isGameOver) {
            // GAME OVER SCREEN
            Text("Game Over!", fontSize = 28.sp, color = Color.Red)
            Spacer(Modifier.height(8.dp))
            Text("Final Score: $score", fontSize = 20.sp)
            Text("Level Reached: $level", fontSize = 20.sp)
            Spacer(Modifier.height(20.dp))
            Button(onClick = { viewModel.startGame() }) {
                Text("Restart")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onBack) {
                Text("Back")
            }
        } else {


            Text("Level: $level   Score: $score")
            Text(text = "Time Left: $timeLeft sec", fontSize = 20.sp, color = Color.Red)
            Spacer(Modifier.height(16.dp))

            when (val q = question) {
                is Question.MissingNumber -> {
                    val leftText = if (q.missingPosition == 1) "_" else q.left.toString()
                    val rightText = if (q.missingPosition == 2) "_" else q.right.toString()

                    Text(
                        "$leftText ${q.operator} $rightText = ${
                            when (q.operator) {
                                '+' -> q.left + q.right
                                '-' -> q.left - q.right
                                '×' -> q.left * q.right
                                '÷' -> if (q.right != 0) q.left / q.right else 0
                                else -> q.left + q.right
                            }
                        }"
                    )

                    OutlinedTextField(
                        value = textAnswer,
                        onValueChange = { textAnswer = it },
                        label = { Text("Answer") }
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        val ans = textAnswer.toIntOrNull()
                        if (ans == null) message = "Enter a number"
                        else {
                            viewModel.submitAnswer(ans)
                            textAnswer = ""
                            message =
                                if (ans == q.answer) "Correct!" else "Wrong, answer: ${q.answer}"
                        }
                    }) { Text("Submit") }
                }

                is Question.MissingOperator -> {
                    Text("${q.a} ? ${q.b} = ${q.result}")
                    Row(Modifier.padding(top = 8.dp)) {
                        q.options.forEach { op ->
                            Button(onClick = {
                                viewModel.submitAnswer(op)
                                message = if (op == q.answer) "Correct!" else "Wrong"
                            }, modifier = Modifier.padding(4.dp)) { Text(op.toString()) }
                        }
                    }
                }

                is Question.TrueFalse -> {
                    Text(q.expression)
                    Row(Modifier.padding(top = 8.dp)) {
                        Button(onClick = {
                            viewModel.submitAnswer(true); message =
                            if (q.isCorrect) "Correct!" else "Wrong"
                        }) { Text("✔") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            viewModel.submitAnswer(false); message =
                            if (!q.isCorrect) "Correct!" else "Wrong"
                        }) { Text("✖") }
                    }
                }

                is Question.Reverse -> {
                    Text("${q.a} _ ${q.b} = ${q.result}")
                    Row(Modifier.padding(top = 8.dp)) {
                        q.options.forEach { op ->
                            Button(onClick = {
                                viewModel.submitAnswer(op); message =
                                if (op == q.answer) "Correct!" else "Wrong"
                            }, modifier = Modifier.padding(4.dp)) {
                                Text(op.toString())
                            }
                        }
                    }
                }

                is Question.Mix -> {
                    Text("Mix mode:")
                    // You can render inner similarly or call viewModel.startNext() to unwrap; for brevity we show inner as text
                    Text(q.inner.toString())
                }

                null -> Text("Loading...")
            }

            Spacer(Modifier.height(12.dp))
            Text(message)
            Spacer(Modifier.height(20.dp))
            Button(onClick = onBack) { Text("Back") }
        }
    }
}
