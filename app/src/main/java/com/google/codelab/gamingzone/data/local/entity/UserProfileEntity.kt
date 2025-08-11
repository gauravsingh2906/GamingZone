package com.google.codelab.gamingzone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.codelab.gamingzone.data.model.GameStats
import java.util.UUID


@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val avatarId: Int,
    val level: Int = 1,
    val unlockedAvatars: List<Int> = emptyList(),
    val currentXP: Int = 0,
    val xpToNextLevel: Int = 100,
    val gamesPlayed: Int = 0,
    val highestScore: Int = 0,
    val totalPlayTimeInMinutes: Int = 0,
    val renameTickets: Int = 1,
    val gameStats:Map<String, GameStats> = emptyMap()
)
