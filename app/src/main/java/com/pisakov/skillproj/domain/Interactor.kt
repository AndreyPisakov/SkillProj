package com.pisakov.skillproj.domain

import com.pisakov.skillproj.data.*
import com.pisakov.skillproj.data.entity.Film
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi, private val preferences: PreferenceProvider) {
    fun getFilmsFromApi(page: Int, category: String, callback: ApiCallback) {
        retrofitService.getFilms(category, API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                val list = response.body()?.tmdbFilms?.map {
                    Film(
                        id = it.id,
                        title = it.title,
                        poster = it.posterPath,
                        description = it.overview,
                        rating = it.voteAverage,
                        isInFavorites = false)
                } as List<Film>
                repo.deleteCache(category)
                repo.saveCache(list, category)
                callback.onSuccess(list)
            }
            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) { callback.onFailure() }
        })
    }

    fun getFilmsFromDB(category: String = getDefaultCategoryFromPreferences()): Flow<List<Film>> = repo.getFilmsWithCategory(category)
    fun getFavoriteFilmsFromDB(): Flow<List<Film>> = repo.getFavoriteFromDB()
    fun updateFilmInDB(film: Film) = repo.updateFilmInDB(film)

    fun saveDefaultCategoryToPreferences(category: String) { preferences.saveDefaultCategory(category) }
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun registerSharedPrefListener(change: () -> Unit) { preferences.registerSharedPrefListener(change) }
    fun unregisterSharedPrefListener(){ preferences.unregisterSharedPrefListener() }
}

interface ApiCallback {
    fun onSuccess(films: List<Film>)
    fun onFailure()
}