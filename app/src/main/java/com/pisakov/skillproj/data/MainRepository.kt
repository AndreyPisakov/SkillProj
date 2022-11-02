package com.pisakov.skillproj.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.pisakov.skillproj.data.dao.FilmDao
import com.pisakov.skillproj.data.entity.Category
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.utils.Selections
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {

    fun putToDb(films: List<Film>, category: String) {
        val listCategory = mutableListOf<Category>()
        Log.d("MyLog", "!repo")
        films.forEach {
            val c = Category(filmId = it.id)
            when (category) {
                Selections.TOP_RATED_CATEGORY -> c.isTopRated = true
                Selections.POPULAR_CATEGORY -> c.isPopular = true
                Selections.NOW_PLAYING_CATEGORY -> c.isNowPlaying = true
                Selections.UPCOMING_CATEGORY -> c.isUpcoming = true
            }
            listCategory.add(c)
        }
        Executors.newSingleThreadExecutor().execute {
            Log.d("MyLog", "!otherThread")
            filmDao.insertAll(films)
            filmDao.insertCategory(listCategory)
        }
    }

    fun getFilmsWithCategory(category: String): LiveData<List<Film>> {
        Log.d("MyLog", "repos")
        return when (category) {
            Selections.TOP_RATED_CATEGORY -> filmDao.getTopRated()
            Selections.POPULAR_CATEGORY -> filmDao.getPopular()
            Selections.NOW_PLAYING_CATEGORY -> filmDao.getNowPlaying()
            Selections.UPCOMING_CATEGORY -> filmDao.getUpcoming()
            else -> {
                Log.d("MyLog", "else")
                filmDao.getCachedFilms() }
        }
    }

    fun getAllFromDB(): LiveData<List<Film>> = filmDao.getCachedFilms()

    fun getFavoriteFromDB(): LiveData<List<Film>> = filmDao.getFavoriteCachedFilms()

    fun updateFilmInDB(film: Film) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.insert(film)
        }
    }

    fun clearTable() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.clearAll()
        }
    }
}