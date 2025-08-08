package com.google.codelab.gamingzone.domain.repository

import com.google.codelab.gamingzone.data.local.entity.UserProfileEntity

interface UserProfileRepository {


    suspend fun getProfile(): UserProfileEntity?
    suspend fun saveProfile(profile: UserProfileEntity)

    suspend fun updateProfile(profile: UserProfileEntity)


}