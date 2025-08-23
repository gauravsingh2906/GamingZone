package com.google.codelab.gamingzone.data.local2.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.toLowerCase
import com.google.codelab.gamingzone.data.local2.UserType
import com.google.codelab.gamingzone.data.local2.dao.DailyMissionDao
import com.google.codelab.gamingzone.data.local2.entity.DailyMissionEntity
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class DailyMissionRepositoryImpl @Inject constructor(
    private val dao: DailyMissionDao,
    private val statsRepository: StatsRepository
) : DailyMissionRepository {

    private var lastMissionDate: String? = null

    private fun todayDate(): String = LocalDate.now().toString()

//    override suspend fun getTodayMissions(userId: String): List<DailyMissionEntity> {
//        val todayDate = LocalDate.now().toString()
//        val existingMissions = dao.getMissionsForDate(todayDate)
//
//        return if (existingMissions.isNotEmpty()) {
//            existingMissions
//        } else {
//            // No missions for today -> generate new based on user profile
//            val profile = userProfileRepository.getProfile(userId)
//
//            val newMissions = generateMissionsForProfile(profile, todayDate)
//            dao.insertMissions(newMissions)
//
//            newMissions
//        }
//    }


    override suspend fun getMissionsForToday(userId: String): List<DailyMissionEntity> {
        val today = todayDate()
        //    val existing = dao.getMissionsForDate(today)

        // If date changed since last fetch, clear missions to force reload from DB or regenerate
        if (lastMissionDate != today) {
            lastMissionDate = today
            // Optionally: clean old missions or keep for stats
        }

        val existing = dao.getMissionsForDate(userId, today)
        if (existing.isNotEmpty()) {
            return existing
        }

        val profile = statsRepository.getProfile(userId)
            ?: throw IllegalStateException("User profile required for generating missions")

        val generatedMissions = generateMissionsForProfile(profile, today)
        dao.insertMissions(generatedMissions)

        return generatedMissions


//        return if (existing.isEmpty()) {
//            val missions = listOf(
//                DailyMissionEntity(date = today, gameName = "Overall", requiredMinutes = 60),
//                DailyMissionEntity(date = today, gameName = "sudoku", requiredMinutes = 20),
//                DailyMissionEntity(date = today, gameName = "algebra", requiredMinutes = 30)
//            )
//            dao.insertMissions(missions)
//            missions
//        } else {
//            existing
//        }
    }

//    private fun generateMissionsForProfile(profile: OverallProfileEntity, date: String): List<DailyMissionEntity> {
//        // Classify user as beginner/intermediate/advanced
//        val userType = classifyUser(profile)
//
//        // Templates for each user type with predefined minutes per game
//        val baseMissions = when(userType) {
//            UserType.BEGINNER -> listOf(
//                DailyMissionEntity(date = date, gameName = "sudoku", requiredMinutes = 15),
//                DailyMissionEntity(date = date, gameName = "algebra", requiredMinutes = 20),
//                DailyMissionEntity(date = date, gameName = "overall", requiredMinutes = 30),
//            )
//            UserType.INTERMEDIATE -> listOf(
//                DailyMissionEntity(date = date, gameName = "sudoku", requiredMinutes = 10),
//                DailyMissionEntity(date = date, gameName = "algebra", requiredMinutes = 25),
//                DailyMissionEntity(date = date, gameName = "math_memory", requiredMinutes = 20),
//            )
//            UserType.ADVANCED -> listOf(
//                DailyMissionEntity(date = date, gameName = "sudoku", requiredMinutes = 20),
//                DailyMissionEntity(date = date, gameName = "algebra", requiredMinutes = 30),
//                DailyMissionEntity(date = date, gameName = "math_memory", requiredMinutes = 30),
//            )
//        }
//
//        // Optional: shuffle or tweak values for more variety if desired
//
//        return baseMissions
//    }

    private fun classifyUser(profile: OverallProfileEntity): UserType {
        return when {
            profile.totalGamesPlayed < 10 -> UserType.BEGINNER
            profile.overallHighestLevel < 20 -> UserType.INTERMEDIATE
            else -> UserType.ADVANCED
        }
    }

    private fun generateMissionsForProfile(
        profile: OverallProfileEntity,
        date: String
    ): List<DailyMissionEntity> {
        val userType = when {
            profile.totalGamesPlayed < 10 -> "beginner"
            profile.overallHighestLevel < 20 -> "intermediate"
            else -> "advanced"
        }
        val userId = profile.userId
        return when (userType) {
            "beginner" -> listOf(
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "sudoku",
                    missionType = "play_games",
                    targetCount = 3,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "easy",
                    description = "Play 3 Easy Sudoku games"
                ),
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "algebra",
                    missionType = "play_games",
                    targetCount = 1,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "easy",
                    description = "Play 1 Algebra game"
                ),
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "overall",
                    missionType = "play_time",
                    targetCount = 30,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "easy",
                    description = "Play math games for 30 minutes"
                )
            )

            "intermediate" -> listOf(
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "sudoku",
                    missionType = "play_games",
                    targetCount = 2,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "medium",
                    description = "Play 2 Sudoku games"
                ),
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "algebra",
                    missionType = "win_games",
                    targetCount = 1,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "medium",
                    description = "Win 1 Algebra game"
                ),
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "math_memory",
                    missionType = "complete_levels",
                    targetCount = 3,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "medium",
                    description = "Complete 3 Math Memory levels"
                )
            )

            else -> listOf(
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "sudoku",
                    missionType = "win_games",
                    targetCount = 2,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "hard",
                    description = "Win 2 Sudoku games"
                ),
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "algebra",
                    missionType = "complete_levels",
                    targetCount = 5,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "hard",
                    description = "Complete 5 Algebra levels"
                ),
                DailyMissionEntity(
                    date = date,
                    userId = userId,
                    gameName = "math_memory",
                    missionType = "play_games",
                    targetCount = 5,
                    progressCount = 0,
                    isCompleted = false,
                    difficulty = "hard",
                    description = "Play 5 Math Memory games"
                )
            )
        }
    }

    override suspend fun updateMissionProgress(
        userId: String,
        gameName: String,
        missionType: String,
        incrementBy: Int
    ) {
        val today = todayDate()
        val missions = dao.getMissionsForDate(userId, today)
        missions.filter { it.gameName.toLowerCase() == gameName && it.missionType == missionType }
            .forEach { mission ->
                val newProgress =
                    (mission.progressCount + incrementBy).coerceAtMost(mission.targetCount)
                val completed = newProgress >= mission.targetCount
                dao.updateMission(
                    mission.copy(
                        progressCount = newProgress,
                        isCompleted = completed
                    )
                )
            }

        missions.find { it.gameName.toLowerCase() == "overall" }?.let { overall ->
            val updatedProgress = overall.progressCount + incrementBy
            val completed = updatedProgress >= overall.targetCount
            dao.updateMission(
                overall.copy(
                    progressCount = updatedProgress,
                    isCompleted = completed
                )
            )
        }

    }

//    override suspend fun updateMissionProgress(gameName: String, minutesPlayed: Int) {
//        val today = todayDate()
//        val missions = dao.getMissionsForDate(today)
//
//        val minutes = minutesPlayed/60
//
//
//        Log.d("Progress", minutesPlayed.toString())
//        missions.find { it.gameName.toLowerCase(Locale.ROOT) == gameName }?.let { mission ->
//            val updatedProgress = mission.progressMinutes + minutes
//            Log.d("Progress", minutesPlayed.toString())
//            Log.d("Progress", updatedProgress.toString())
//            val completed = updatedProgress >= mission.requiredMinutes
//            dao.updateMission(
//                mission = mission.copy(
//                    progressMinutes = updatedProgress,
//                    isCompleted = completed
//                )
//            )
//        }
//
//        // Also update overall mission
//        missions.find { it.gameName == "Overall" }?.let { overall ->
//            val updatedProgress = overall.progressMinutes + minutesPlayed
//            val completed = updatedProgress >= overall.requiredMinutes
//            dao.updateMission(
//                overall.copy(
//                    progressMinutes = updatedProgress,
//                    isCompleted = completed
//                )
//            )
//        }
//    }
}

