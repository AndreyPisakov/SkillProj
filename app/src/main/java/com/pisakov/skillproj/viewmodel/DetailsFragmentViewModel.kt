package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import javax.inject.Inject

class DetailsFragmentViewModel: ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun updateFilm(film: Film) {
        interactor.updateFilmInDB(film)
    }
}