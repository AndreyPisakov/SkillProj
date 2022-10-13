package com.pisakov.skillproj.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("3/movie/popular")
    fun getPopularFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>

    @GET("3/movie/latest")
    fun getLatestFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Call<TmdbResultsDto>

    @GET("3/movie/top_rated")
    fun getTopRatedFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>

    @GET("3/movie/now_playing")
    fun getNowPlayingFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>

    @GET("3/movie/upcoming")
    fun getUpcomingFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>
}
