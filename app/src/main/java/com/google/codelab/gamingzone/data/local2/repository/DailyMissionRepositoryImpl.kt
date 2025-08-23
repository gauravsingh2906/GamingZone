package com.google.codelab.gamingzone.data.local2.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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


    override suspend fun getMissionsForToday(userId:String): List<DailyMissionEntity> {
        val today = todayDate()
    //    val existing = dao.getMissionsForDate(today)

        // If date changed since last fetch, clear missions to force reload from DB or regenerate
        if (lastMissionDate != today) {
            lastMissionDate = today
            // Optionally: clean old missions or keep for stats
        }

        val existing = dao.getMissionsForDate(today)
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

    private fun generateMissionsForProfile(profile: OverallProfileEntity, date: String): List<DailyMissionEntity> {
        // Classify user as beginner/intermediate/advanced
        val userType = classifyUser(profile)

        // Templates for each user type with predefined minutes per game
        val baseMissions = when(userType) {
            UserType.BEGINNER -> listOf(
                DailyMissionEntity(date = date, gameName = "sudoku", requiredMinutes = 15),
                DailyMissionEntity(date = date, gameName = "algebra", requiredMinutes = 20),
                DailyMissionEntity(date = date, gameName = "overall", requiredMinutes = 30),
            )
            UserType.INTERMEDIATE -> listOf(
                DailyMissionEntity(date = date, gameName = "sudoku", requiredMinutes = 10),
                DailyMissionEntity(date = date, gameName = "algebra", requiredMinutes = 25),
                DailyMissionEntity(date = date, gameName = "math_memory", requiredMinutes = 20),
            )
            UserType.ADVANCED -> listOf(
                DailyMissionEntity(date = date, gameName = "sudoku", requiredMinutes = 20),
                DailyMissionEntity(date = date, gameName = "algebra", requiredMinutes = 30),
                DailyMissionEntity(date = date, gameName = "math_memory", requiredMinutes = 30),
            )
        }

        // Optional: shuffle or tweak values for more variety if desired

        return baseMissions
    }

    private fun classifyUser(profile: OverallProfileEntity): UserType {
        return when {
            profile.totalGamesPlayed < 10 -> UserType.BEGINNER
            profile.overallHighestLevel < 20 -> UserType.INTERMEDIATE
            else -> UserType.ADVANCED
        }
    }

    override suspend fun updateMissionProgress(gameName: String, minutesPlayed: Int) {
        val today = todayDate()
        val missions = dao.getMissionsForDate(today)


        Log.d("Progress", minutesPlayed.toString())
        missions.find { it.gameName.toLowerCase(Locale.ROOT) == gameName }?.let { mission ->
            val updatedProgress = mission.progressMinutes + minutesPlayed
            Log.d("Progress", minutesPlayed.toString())
            Log.d("Progress", updatedProgress.toString())
            val completed = updatedProgress >= mission.requiredMinutes
            dao.updateMission(
                mission = mission.copy(
                    progressMinutes = updatedProgress,
                    isCompleted = completed
                )
            )
        }

        // Also update overall mission
        missions.find { it.gameName == "Overall" }?.let { overall ->
            val updatedProgress = overall.progressMinutes + minutesPlayed
            val completed = updatedProgress >= overall.requiredMinutes
            dao.updateMission(
                overall.copy(
                    progressMinutes = updatedProgress,
                    isCompleted = completed
                )
            )
        }
    }
}

