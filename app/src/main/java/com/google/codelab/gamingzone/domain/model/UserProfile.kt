package com.google.codelab.gamingzone.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


data class UserProfile(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val avatarId: Int,
    val level: Int=1,
    val gamesPlayed: Int=0,
    val highestScore: Int=0,
    val totalPlayTimeInMinutes:Int=0,
    val renameTickets: Int=1
)
//  val unlockedAvatars: List<Int> = emptyList(),