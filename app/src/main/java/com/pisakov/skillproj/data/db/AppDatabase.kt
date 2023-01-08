package com.pisakov.skillproj.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pisakov.skillproj.data.dao.FilmDao
import com.pisakov.skillproj.data.dao.NotificationDao
import com.pisakov.skillproj.data.entity.Category
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.data.entity.Notification

@Database(entities = [Film::class, Category::class, Notification::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun notificationDao(): NotificationDao
}