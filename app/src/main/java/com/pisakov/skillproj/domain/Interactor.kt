package com.pisakov.skillproj.domain

import com.pisakov.remote_module.TmdbApi
import com.pisakov.skillproj.data.*
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.data.entity.Notification
import com.pisakov.skillproj.utils.Converter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi, private val preferences: PreferenceProvider) {

    fun loadFilmsToDB(list: List<Film>, category: String) = repo.saveCache(list, category)

    fun getFilmsFromApi(page: Int, category: String): Observable<List<Film>> =
        retrofitService.getFilms(category, API.KEY, "ru-RU", page)
            .subscribeOn(Schedulers.io())
            .map { Converter.convertApiListToDtoList(it.tmdbFilms) }

    fun getFilmsFromQueryApi(page: Int, query: String): Observable<List<Film>> =
        retrofitService.getQuery(API.KEY, "ru-RU", query, page)
            .map { Converter.convertApiListToDtoList(it.tmdbFilms) }

    fun getFilmsFromId(id: List<Int>): Observable<List<Film>> = repo.getFilmsFromId(id)
    fun getFilmsFromDB(category: String = getDefaultCategoryFromPreferences()): Observable<List<Film>> = repo.getFilmsWithCategory(category)
    fun getFavoriteFilmsFromDB(): Observable<List<Film>> = repo.getFavoriteFromDB()
    fun updateFilmInDB(film: Film) = repo.updateFilmInDB(film)

    fun saveDefaultCategoryToPreferences(category: String) { preferences.saveDefaultCategory(category) }
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun registerSharedPrefListener(change: () -> Unit) { preferences.registerSharedPrefListener(change) }
    fun unregisterSharedPrefListener(){ preferences.unregisterSharedPrefListener() }

    fun insertNotification(notification: Notification) { repo.insertNotification(notification) }
    fun editNotification(notification: Notification) { repo.updateNotification(notification) }
    fun deleteNotification(id: Int) { repo.deleteNotification(id) }
    fun getNotificationList(): Observable<List<Notification>> = repo.getNotificationList()
}