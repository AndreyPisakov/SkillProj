package com.pisakov.skillproj.data.dao

import androidx.room.*
import com.pisakov.skillproj.data.entity.Category
import com.pisakov.skillproj.data.entity.Film
import io.reactivex.rxjava3.core.Observable

@Dao
interface FilmDao {
    @Query("SELECT * FROM cached_films WHERE is_in_favorites = 1")
    fun getFavoriteCachedFilms(): Observable<List<Film>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film)

    @Query("DELETE FROM cached_films WHERE id IN (:id)")
    fun deleteFilms(id: List<Int>)

    @Query("SELECT * FROM cached_films JOIN category ON cached_films.id = category.film_id AND " +
            "CASE " +
            "WHEN :category = 1 THEN category.is_popular = 1 " +
            "WHEN :category = 2 THEN category.is_top_rated = 1 " +
            "WHEN :category = 3 THEN category.is_now_playing = 1 " +
            "WHEN :category = 4 THEN category.is_upcoming = 1 " +
            "END")
    fun getFilmsFromCategory(category : Int): Observable<List<Film>>

    //////////CATEGORY//////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(list: List<Category>)

    @Query("SELECT film_id FROM category WHERE " +
            "CASE " +
            "WHEN :category = 1 THEN is_popular = 1 " +
            "WHEN :category = 2 THEN is_top_rated = 1 " +
            "WHEN :category = 3 THEN is_now_playing = 1 " +
            "WHEN :category = 4 THEN is_upcoming = 1 " +
            "END")
    fun getId(category : Int): List<Int>
}