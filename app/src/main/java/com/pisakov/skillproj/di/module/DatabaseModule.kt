package com.pisakov.skillproj.di.module

import android.content.Context
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.data.db.DatabaseHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}