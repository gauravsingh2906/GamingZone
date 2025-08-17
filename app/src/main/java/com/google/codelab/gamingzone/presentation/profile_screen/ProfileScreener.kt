package com.google.codelab.gamingzone.presentation.profile_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import com.google.codelab.gamingzone.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreener(viewModel: UserStatsViewModel = hiltViewModel()) {
    val userId by viewModel.userId.collectAsState()
    val totalStats by viewModel.totalStatsFlow.collectAsState()
    val perGameStats by viewModel.perGameStatsFlow.collectAsState()
    val advancedStats by viewModel.advancedStatsFlow.collectAsState()

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text(text = "User ID: ${userId ?: "initializing..."}")
        Spacer(Modifier.height(8.dp))
        Text(text = "Total games: ${totalStats?.totalGamesPlayed ?: 0}")
        Text(text = "Total wins: ${totalStats?.totalWins ?: 0}")
        Text(text = "Total losses: ${totalStats?.totalLosses ?: 0}")
        Text(text = "Total XP: ${totalStats?.totalXP ?: 0}")

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Per-game stats:")

        perGameStats.forEach { g ->
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${g.gameName} — played: ${g.gamesPlayed}, wins: ${g.wins}, losses: ${g.losses}, xp: ${g.xp}")
        }

        Spacer(modifier = Modifier.height(12.dp))
        advancedStats.forEach {
            Text(
                text = "${it.gameType} — played: ${it.totalPlayed}, wins: ${it.totalCorrect}, losses: ${it.totalWrong}, xp: ${it.xpEarned},bestStreak: ${it.bestStreak}, lastStreak: ${it.lastStreak}, time: ${it.totalTimeSeconds}, avgTime: ${it.totalTimeSeconds/it.totalPlayed}, avg: ${it.xpEarned/it.totalPlayed}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Example action: record a Chess win +50xp
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.recordGameResult("CHESS", isWin = true, isDraw = false, xpEarned = 50) }) {
                Text("Record Chess Win (+50 XP)")
            }
            Button(onClick = { viewModel.recordGameResult("SUDOKU", isWin = false, isDraw = false, xpEarned = 10) }) {
                Text("Record Sudoku Loss (+10 XP)")
            }
        }
    }
}
