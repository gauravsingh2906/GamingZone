package com.google.codelab.gamingzone.di

import android.app.Application
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideSensorManager(app: Application): SensorManager {
        return app.getSystemService(SensorManager::class.java)
    }
}
