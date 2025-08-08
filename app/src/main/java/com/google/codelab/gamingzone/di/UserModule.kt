package com.google.codelab.gamingzone.di

import android.content.Context
import androidx.room.Room
import com.google.codelab.gamingzone.data.local.AppDatabase
import com.google.codelab.gamingzone.data.local.dao.GameResultDao
import com.google.codelab.gamingzone.data.local.dao.UserProfileDao
import com.google.codelab.gamingzone.data.repository.GameResultRepositoryImpl
import com.google.codelab.gamingzone.data.repository.UserProfileRepositoryImpl
import com.google.codelab.gamingzone.domain.repository.GameResultRepository
import com.google.codelab.gamingzone.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_profile"
        ).build()
    }

    @Provides
    fun provideUserProfileDao(db: AppDatabase): UserProfileDao = db.userProfileDao()


    @Provides
    fun provideUserProfileRepository(dao: UserProfileDao): UserProfileRepository =
        UserProfileRepositoryImpl(dao)


//    @Provides
//    @Singleton
//    fun provideGameDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java,
//            "game_db"
//        ).build()
//    }

    @Provides
    fun provideGameResultDao(db: AppDatabase): GameResultDao {
        return db.gameResultDao()
    }

    @Provides
    fun provideGameResultRepository(dao: GameResultDao): GameResultRepository {
        return GameResultRepositoryImpl(dao)
    }

}