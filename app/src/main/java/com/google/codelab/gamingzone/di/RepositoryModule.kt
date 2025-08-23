package com.google.codelab.gamingzone.di

import com.google.codelab.gamingzone.data.local2.repository.DailyMissionRepository
import com.google.codelab.gamingzone.data.local2.repository.DailyMissionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDailyMissionRepository(
        impl: DailyMissionRepositoryImpl
    ): DailyMissionRepository
}
