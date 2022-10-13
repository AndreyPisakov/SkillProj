package com.pisakov.skillproj.domain

import com.pisakov.skillproj.data.API
import com.pisakov.skillproj.data.TmdbApi
import com.pisakov.skillproj.data.TmdbResultsDto
import com.pisakov.skillproj.utils.Converter
import com.pisakov.skillproj.utils.Selections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(//private val repo: MainRepository,
                 private val retrofitService: TmdbApi) {

    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }

    fun getListFilmsFromApi(type: String, page: Int, callback: ApiCallback){
        val callbackInteractor = object : Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                callback.onFailure()
            }
        }
        when(type) {
            Selections.popular -> {
                retrofitService.getPopularFilms(API.KEY, "ru-RU", page).enqueue(callbackInteractor)
            }
            Selections.latest -> {
                retrofitService.getLatestFilms(API.KEY, "ru-RU").enqueue(callbackInteractor)
            }
            Selections.now_playing -> {
                retrofitService.getNowPlayingFilms(API.KEY, "ru-RU", page).enqueue(callbackInteractor)
            }
            Selections.top_rated -> {
                retrofitService.getTopRatedFilms(API.KEY, "ru-RU", page).enqueue(callbackInteractor)
            }
            Selections.upcoming -> {
                retrofitService.getUpcomingFilms(API.KEY, "ru-RU", page).enqueue(callbackInteractor)
            }
        }
    }
}