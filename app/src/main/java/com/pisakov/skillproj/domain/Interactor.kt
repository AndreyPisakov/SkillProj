package com.pisakov.skillproj.domain

import com.pisakov.skillproj.data.MainRepository

class Interactor(private val repository: MainRepository) {
    fun getFilmsDB(): List<Film> = repository.filmsDataBase
}