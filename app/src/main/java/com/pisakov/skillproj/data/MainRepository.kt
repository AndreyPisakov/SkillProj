package com.pisakov.skillproj.data

import androidx.lifecycle.LiveData
import com.pisakov.skillproj.data.dao.FilmDao
import com.pisakov.skillproj.data.entity.Film
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {

    fun putToDb(films: List<Film>) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getAllFromDB(): List<Film> {
        return filmDao.getCachedFilms()
    }

    fun getFavoriteFromDB(): LiveData<List<Film>> {
        return filmDao.getFavoriteCachedFilms()
    }

    fun updateFilmInDB(film: Film) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.insert(film)
        }
    }
}