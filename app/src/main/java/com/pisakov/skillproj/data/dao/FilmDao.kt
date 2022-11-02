package com.pisakov.skillproj.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pisakov.skillproj.data.entity.Category
import com.pisakov.skillproj.data.entity.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms(): LiveData<List<Film>>

    @Query("SELECT * FROM cached_films WHERE is_in_favorites = 1")
    fun getFavoriteCachedFilms(): LiveData<List<Film>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film)

    @Query("DELETE FROM cached_films")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(list: List<Category>)

    @Query("SELECT * FROM cached_films JOIN category ON is_popular = 1")
    fun getPopular(): LiveData<List<Film>>

    @Query("SELECT * FROM cached_films JOIN category ON is_top_rated = 1")
    fun getTopRated(): LiveData<List<Film>>

    @Query("SELECT * FROM cached_films JOIN category ON is_now_playing = 1")
    fun getNowPlaying(): LiveData<List<Film>>

    @Query("SELECT * FROM cached_films JOIN category ON is_upcoming = 1")
    fun getUpcoming(): LiveData<List<Film>>
}