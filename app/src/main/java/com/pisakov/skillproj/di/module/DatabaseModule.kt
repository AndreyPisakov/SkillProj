package com.pisakov.skillproj.di.module

import android.content.Context
import androidx.room.Room
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.data.dao.FilmDao
import com.pisakov.skillproj.data.dao.NotificationDao
import com.pisakov.skillproj.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideFilmDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        ).fallbackToDestructiveMigration().build().filmDao()

    @Singleton
    @Provides
    fun provideNotificationDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        ).fallbackToDestructiveMigration().build().notificationDao()

    @Provides
    @Singleton
    fun provideRepository(filmDao: FilmDao, notificationDao: NotificationDao) = MainRepository(filmDao, notificationDao)
}