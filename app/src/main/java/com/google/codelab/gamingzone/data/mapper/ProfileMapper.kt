package com.google.codelab.gamingzone.data.mapper

import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity
import com.google.codelab.gamingzone.domain.model.UserProfile


fun UserProfile.toUserProfileEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        username = username,
        avatarId = avatarId,
        level = level,
        gamesPlayed = gamesPlayed,
        highestScore = highestScore,
        totalPlayTimeInMinutes = totalPlayTimeInMinutes,
        renameTickets = renameTickets,
    )
}

fun UserProfileEntity.toUserProfile(): UserProfile {
    return UserProfile(
        id = id,
        username = username,
        avatarId = avatarId,
        level = level,
        gamesPlayed = gamesPlayed,
        highestScore = highestScore,
        totalPlayTimeInMinutes = totalPlayTimeInMinutes.toInt(),
        renameTickets = renameTickets,
    )
}