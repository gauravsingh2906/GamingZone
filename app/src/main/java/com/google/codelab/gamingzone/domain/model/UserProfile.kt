package com.google.codelab.gamingzone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0L,
    val username: String,
    val avatarId: Int,
    val level: Int=1,
    val gamesPlayed: Int=0,
    val highestScore: Int=0,
    val totalPlayTimeInMinutes:Int=0,
    val renameTickets: Int=1
)
//  val unlockedAvatars: List<Int> = emptyList(),