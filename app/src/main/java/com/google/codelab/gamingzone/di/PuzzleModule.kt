package com.google.codelab.gamingzone.di


import android.content.Context
import com.google.codelab.gamingzone.data.LogicPuzzleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PuzzleModule {

    @Provides
    @Singleton
    fun provideLogicPuzzleRepository(
        @ApplicationContext context: Context
    ): LogicPuzzleRepository {
        return LogicPuzzleRepository(context)
    }
}

