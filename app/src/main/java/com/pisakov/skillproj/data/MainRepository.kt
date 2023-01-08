package com.pisakov.skillproj.data

import com.pisakov.skillproj.data.dao.FilmDao
import com.pisakov.skillproj.data.dao.NotificationDao
import com.pisakov.skillproj.data.entity.Category
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.data.entity.Notification
import com.pisakov.skillproj.utils.Selections
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainRepository(private val filmDao: FilmDao, private val notificationDao: NotificationDao) {

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
        Completable.fromAction {
            filmDao.insertAll(films)
            filmDao.insertCategory(listCategory)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun getFilmsWithCategory(category: String): Observable<List<Film>> = filmDao.getFilmsFromCategory(when (category) {
            Selections.TOP_RATED_CATEGORY -> TOP_RATED_CATEGORY_INT
            Selections.POPULAR_CATEGORY -> POPULAR_CATEGORY_INT
            Selections.NOW_PLAYING_CATEGORY -> NOW_PLAYING_CATEGORY_INT
            Selections.UPCOMING_CATEGORY -> UPCOMING_CATEGORY_INT
            else -> ANOTHER_CATEGORY
        })

    fun getFavoriteFromDB(): Observable<List<Film>> = filmDao.getFavoriteCachedFilms()

    fun getFilmsFromId(id: List<Int>): Observable<List<Film>> = filmDao.getFilmFromId(id)

    fun updateFilmInDB(film: Film) {
        Completable.fromAction {
            filmDao.insert(film)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun insertNotification(notification: Notification) {
        Completable.fromAction {
            notificationDao.insertNotification(notification)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun updateNotification(notification: Notification){
        Completable.fromAction {
            notificationDao.updateNotification(notification)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun deleteNotification(id: Int){
        Completable.fromAction {
            notificationDao.deleteNotification(id)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun getNotificationList(): Observable<List<Notification>> = notificationDao.getAllNotifications()

    companion object {
        const val POPULAR_CATEGORY_INT = 1
        const val TOP_RATED_CATEGORY_INT = 2
        const val NOW_PLAYING_CATEGORY_INT = 3
        const val UPCOMING_CATEGORY_INT = 4
        const val ANOTHER_CATEGORY = 20
    }
}