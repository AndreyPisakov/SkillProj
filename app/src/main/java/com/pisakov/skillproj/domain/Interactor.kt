package com.pisakov.skillproj.domain

import com.pisakov.skillproj.data.API
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.data.TmdbApi
import com.pisakov.skillproj.data.TmdbResultsDto
import com.pisakov.skillproj.utils.Converter
import com.pisakov.skillproj.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi) {

    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                callback.onFailure()
            }
        })
    }
}