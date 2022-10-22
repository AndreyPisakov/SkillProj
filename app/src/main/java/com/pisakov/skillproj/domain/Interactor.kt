package com.pisakov.skillproj.domain

import com.pisakov.skillproj.data.*
import com.pisakov.skillproj.utils.Converter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi, private val preferences: PreferenceProvider) {
    fun getFilmsFromApi(page: Int, category: String, callback: ApiCallback) {
        retrofitService.getFilms(category, API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
            }
            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) { callback.onFailure() }
        })
    }

    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun registerSharedPrefListener(change: onSharedPrefChange){
        preferences.registerSharedPrefListener(change)
    }

    interface onSharedPrefChange {
        fun change()
    }
}

interface ApiCallback {
    fun onSuccess(films: List<Film>)
    fun onFailure()
}