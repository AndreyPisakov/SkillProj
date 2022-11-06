package com.pisakov.skillproj.domain

import androidx.lifecycle.LiveData
import com.pisakov.skillproj.data.*
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.utils.Converter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi, private val preferences: PreferenceProvider) {
    fun getFilmsFromApi(page: Int, category: String, callback: ApiCallback) {
        retrofitService.getFilms(category, API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                val list = Converter.convertApiListToDtoList(response.body()?.tmdbFilms)
                repo.deleteCache(category)
                repo.saveCache(list, category)
                callback.onSuccess(list)
            }
            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) { callback.onFailure() }
        })
    }

    fun getFilmsFromDB(category: String = getDefaultCategoryFromPreferences()): List<Film> = repo.getFilmsWithCategory(category)
    fun getFavoriteFilmsFromDB(): LiveData<List<Film>> = repo.getFavoriteFromDB()
    fun updateFilmInDB(film: Film) = repo.updateFilmInDB(film)

    fun saveDefaultCategoryToPreferences(category: String) { preferences.saveDefaultCategory(category) }
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun registerSharedPrefListener(change: onSharedPrefChange){ preferences.registerSharedPrefListener(change) }
    fun unregisterSharedPrefListener(){ preferences.unregisterSharedPrefListener() }

    interface onSharedPrefChange {
        fun change()
    }
}

interface ApiCallback {
    fun onSuccess(films: List<Film>)
    fun onFailure()
}