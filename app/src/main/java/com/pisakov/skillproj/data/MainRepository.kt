package com.pisakov.skillproj.data

import com.pisakov.skillproj.data.dao.FilmDao
import com.pisakov.skillproj.data.entity.Category
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.utils.Selections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainRepository(private val filmDao: FilmDao) {

    val repositoryScope = CoroutineScope(Job())

    fun saveCache(films: List<Film>, category: String) {
        val listCategory = mutableListOf<Category>()
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
        repositoryScope.launch {
            filmDao.insertAll(films)
            filmDao.insertCategory(listCategory)
        }
    }

    fun deleteCache(category: String) {
        repositoryScope.launch {
            val listId = filmDao.getId(when (category) {
                Selections.TOP_RATED_CATEGORY -> TOP_RATED_CATEGORY_INT
                Selections.POPULAR_CATEGORY -> POPULAR_CATEGORY_INT
                Selections.NOW_PLAYING_CATEGORY -> NOW_PLAYING_CATEGORY_INT
                Selections.UPCOMING_CATEGORY -> UPCOMING_CATEGORY_INT
                else -> ANOTHER_CATEGORY
            })
            filmDao.deleteFilms(listId)
        }
    }

    fun getFilmsWithCategory(category: String): Flow<List<Film>> = filmDao.getFilmsFromCategory(when (category) {
            Selections.TOP_RATED_CATEGORY -> TOP_RATED_CATEGORY_INT
            Selections.POPULAR_CATEGORY -> POPULAR_CATEGORY_INT
            Selections.NOW_PLAYING_CATEGORY -> NOW_PLAYING_CATEGORY_INT
            Selections.UPCOMING_CATEGORY -> UPCOMING_CATEGORY_INT
            else -> ANOTHER_CATEGORY
        })

    fun getFavoriteFromDB(): Flow<List<Film>> = filmDao.getFavoriteCachedFilms()

    fun updateFilmInDB(film: Film) {
        repositoryScope.launch {
            filmDao.insert(film)
        }
    }

    companion object {
        const val POPULAR_CATEGORY_INT = 1
        const val TOP_RATED_CATEGORY_INT = 2
        const val NOW_PLAYING_CATEGORY_INT = 3
        const val UPCOMING_CATEGORY_INT = 4
        const val ANOTHER_CATEGORY = 20
    }
}