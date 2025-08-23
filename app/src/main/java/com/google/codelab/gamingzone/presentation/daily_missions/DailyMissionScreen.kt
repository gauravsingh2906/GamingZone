package com.google.codelab.gamingzone.presentation.daily_missions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DailyMissionScreen(viewModel: DailyMissionViewModel = hiltViewModel()) {
    val missions by viewModel.missions.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Daily Missions", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        missions.forEach { mission ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("${mission.gameName} - ${mission.progressMinutes}/${mission.requiredMinutes} min")
                    LinearProgressIndicator(
                        progress = mission.progressMinutes.toFloat() / mission.requiredMinutes,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (mission.isCompleted) {
                        Text("✅ Completed", color = Color.Green)
                    }
                }
            }
        }
    }
}
