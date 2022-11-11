package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import javax.inject.Inject

class FavoriteFragmentViewModel: ViewModel() {
    val filmListLiveData: LiveData<List<Film>>

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        filmListLiveData = interactor.getFavoriteFilmsFromDB()
    }
}