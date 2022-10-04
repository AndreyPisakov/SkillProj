package com.pisakov.skillproj.domain

import android.util.Log
import com.pisakov.skillproj.data.API
import com.pisakov.skillproj.data.TmdbApi
import com.pisakov.skillproj.data.TmdbResultsDto
import com.pisakov.skillproj.utils.Converter
import com.pisakov.skillproj.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(//private val repo: MainRepository,
                 private val retrofitService: TmdbApi) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
                Log.d("MyLog", "interactor success")
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                Log.d("MyLog", "interactor error")
                callback.onFailure()
            }
        })
    }
}