package com.google.codelab.gamingzone.data.repository

import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity

interface UserProfileRepository {

    suspend fun insertOrUpdateProfile(profile: UserProfileEntity): Long

    suspend fun getProfileById(): UserProfileEntity?

    



}