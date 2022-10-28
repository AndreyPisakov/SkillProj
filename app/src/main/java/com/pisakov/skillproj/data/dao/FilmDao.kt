package com.pisakov.skillproj.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pisakov.skillproj.data.entity.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms(): List<Film>

    @Query("SELECT * FROM cached_films WHERE is_in_favorites = 1")
    fun getFavoriteCachedFilms(): LiveData<List<Film>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film)
}