package com.google.codelab.gamingzone.presentation.daily_missions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.codelab.gamingzone.presentation.profile_stats.StatsViewModel

@Composable
fun DailyMissionScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val missions by viewModel.missions.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        Text("Daily Missions", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(18.dp))
        missions.forEach { mission ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mission.isCompleted) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(36.dp)
                        )
                    } else {
                        CircularProgressIndicator(
                            progress = mission.progressCount / mission.targetCount.toFloat(),
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 4.dp
                        )
                    }
                    Spacer(Modifier.width(18.dp))
                    Column(Modifier.weight(1f)) {
                        Text(mission.description, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = mission.progressCount / mission.targetCount.toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = if (mission.isCompleted) Color(0xFF4CAF50) else Color(0xFF29B6F6)
                        )
                        Spacer(Modifier.height(3.dp))
                        Text("${mission.progressCount} / ${mission.targetCount}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
