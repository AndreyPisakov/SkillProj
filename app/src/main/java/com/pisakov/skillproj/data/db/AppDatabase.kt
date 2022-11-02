package com.pisakov.skillproj.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pisakov.skillproj.data.dao.FilmDao
import com.pisakov.skillproj.data.entity.Category
import com.pisakov.skillproj.data.entity.Film

@Database(entities = [Film::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}