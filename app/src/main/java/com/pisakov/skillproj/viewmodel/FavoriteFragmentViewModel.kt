package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.Interactor
import javax.inject.Inject

class FavoriteFragmentViewModel: ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun getFilmList() = interactor.getFavoriteFilmsFromDB()
}