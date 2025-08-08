package com.google.codelab.gamingzone.presentation.games.logic_puzzle

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.google.codelab.gamingzone.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("MissingPermission")
@Composable
fun LogicPuzzleScreen(viewModel: LogicPuzzleViewModel= hiltViewModel()) {
    val context = LocalContext.current
    val puzzle by viewModel.currentPuzzle.collectAsState()
    val selectedName by viewModel.selectedName.collectAsState()
    val isCorrect by viewModel.isAnswerCorrect.collectAsState()
    val revealTruth by viewModel.revealTruth.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect{ event ->
            when (event) {
                is LogicPuzzleViewModel.UIEvent.PlayRevealSoundAndVibrate -> {
                    // ✅ Play sound
                    val mediaPlayer = MediaPlayer.create(context,R.raw.win_sound)
                    mediaPlayer.start()

                    // ✅ Vibrate
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                        )
                    } else {
                        vibrator.vibrate(100)
                    }
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.loadPuzzles()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        puzzle?.let {
            Text("Level ${it.level}", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(it.context)

            Spacer(Modifier.height(16.dp))
            it.characters.forEach { char ->

                val isSelected = selectedName == char.name
                val isTrue = char.truth

                val bgColor = when {
                    revealTruth && isTrue -> Color.Green.copy(alpha = 0.3f)
                    isSelected -> Color.Blue.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.selectCharacter(char.name) },
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier
                        .background(bgColor).padding(8.dp)) {
                        Row {
                            Text(char.name, fontWeight = FontWeight.Bold)

                            if (revealTruth) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    if (char.truth) "✅" else "❌",
                                    color = if (char.truth) Color.Green else Color.Red
                                )
                            }
                        }
                        Text("“${char.statement}”")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Button(onClick = { viewModel.checkAnswer() }) {
                    Text("Check Answer")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.revealTruth() }) {
                    Text("Reveal Truth")
                }
                if (revealTruth) {
                    Text(
                        "Truth revealed!",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

            }
            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.prevPuzzle()
                        viewModel.resetAnswer()
                    },
                    enabled = viewModel.canGoBack()
                ) {
                    Text("⬅ Previous")
                }

                Button(
                    onClick = {
                        viewModel.nextPuzzle()
                        viewModel.resetAnswer()
                    },
                    enabled = viewModel.canGoForward()
                ) {
                    Text("Next ➡")
                }
            }


            if (isCorrect != null) {
                Text(
                    text = if (isCorrect == true) "✅ Correct!" else "❌ Wrong!",
                    color = if (isCorrect == true) Color.Green else Color.Red,
                    fontSize = 18.sp
                )
            }
        } ?: Text("Loading...")
    }
}
