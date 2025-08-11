package com.google.codelab.gamingzone.data.local1.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val username: String? = "Player",
    val createdAt: Long = System.currentTimeMillis()
)