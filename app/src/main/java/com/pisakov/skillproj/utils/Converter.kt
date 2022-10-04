package com.pisakov.skillproj.utils

import com.pisakov.skillproj.data.TmdbFilm
import com.pisakov.skillproj.domain.Film

object Converter {
    fun convertApiListToDtoList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                title = it.title,
                poster = it.posterPath,
                description = it.overview,
                rating = it.voteAverage,
                isInFavorites = false
                )
            )
        }
        return result
    }
}