package com.google.codelab.gamingzone.data.local2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.codelab.gamingzone.data.local1.entity.UserEntity
import com.google.codelab.gamingzone.data.local2.entity.OverallProfileEntity

@Dao
interface OverallProfileDao {

    @Query("SELECT * FROM overall_profile LIMIT 1")
    suspend fun getAnyUser(): OverallProfileEntity?

    @Query("SELECT * FROM overall_profile WHERE userId = :userId LIMIT 1")
    suspend fun getProfile(userId: String): OverallProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: OverallProfileEntity)

    @Update
    suspend fun updateProfile(profile: OverallProfileEntity)
}