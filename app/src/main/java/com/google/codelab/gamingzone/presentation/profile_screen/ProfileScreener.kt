package com.google.codelab.gamingzone.presentation.profile_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import com.google.codelab.gamingzone.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
fun ProfileScreener(
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val profile = viewModel.profileState.value

    var username by remember { mutableStateOf(profile?.username ?: "") }
  //  var avatar by remember { mutableStateOf(profile?.avatarId ?: "") }
    var selectedAvatarId by remember { mutableStateOf(profile?.avatarId ?: R.drawable.enemy1) }

    // Update values when profile loads
    LaunchedEffect(profile) {
        profile?.let {
            username = it.username
            selectedAvatarId = it.avatarId
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (profile == null) "Create Profile" else "Edit Profile",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(selectedAvatarId),
            contentDescription = null
        )

        Spacer(Modifier.height(16.dp))

//        Text("Choose Avatar:")
//        Row {
//            (1..5).forEach { avatarId ->
//                Icon(
//                    painter = painterResource(id = R.drawable.enemy1),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(64.dp)
//                        .padding(4.dp)
//                        .clickable { selectedAvatarId = avatarId }
//                        .border(
//                            width = if (selectedAvatarId == avatarId) 2.dp else 0.dp,
//                            color = Color.Blue,
//                            shape = CircleShape
//                        )
//                )
//            }
//        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveProfile(username, avatarId = selectedAvatarId , unlockedAvatars = profile?.unlockedAvatars
                    ?: listOf(R.drawable.enemy1))
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (profile == null) "Create" else "Update")
        }

        Spacer(Modifier.height(16.dp))


        Text("Username: ${profile?.username}")
        Text("Level: ${profile?.level}")
        Text("XP: ${profile?.currentXP} / ${profile?.xpToNextLevel}")
        Text("Games Played: ${profile?.gamesPlayed}")
        Text("Highest Score: ${profile?.highestScore}")
        Text("Total Play Time: ${profile?.totalPlayTimeInMinutes} mins")
        Text("Rename Tickets: ${profile?.renameTickets}")

        if (profile != null) {
            Text("Level: ${profile.level}")
           LinearProgressIndicator(
                progress = (profile.totalPlayTimeInMinutes % 100) / 100f,
                modifier = Modifier.fillMaxWidth()
            )
            Text("TotalPlayTime: ${profile.totalPlayTimeInMinutes % 100} / 100")
        }
    }

    profile?.let {
        val sudokuStats = profile.gameStats["sudoku"]

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (sudokuStats!=null) {
                Text("TotalGames: ${sudokuStats.gamesPlayed}")
                Text("Wins: ${sudokuStats.wins}")
                Text("Losses: ${sudokuStats.losses}")
                Text("Draws: ${sudokuStats.totalDraws}")
                Text("Easy games played: ${sudokuStats.easyGamesPlayed}")
                Text("Medium games played: ${sudokuStats.mediumGamesPlayed}")
                Text("Hard games played: ${sudokuStats.hardGamesPlayed}")
            } else {
                Text("No sudoku games played yet")
            }
        }
    } ?: run {
        CircularProgressIndicator(modifier = Modifier.padding(12.dp))
    }
}