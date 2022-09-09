package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.Film
import com.pisakov.skillproj.domain.Interactor

class HomeFragmentViewModel : ViewModel() {
    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    private var interactor: Interactor = App.instance.interactor

    init {
        val films = interactor.getFilmsDB()
        _filmListLiveData.postValue(films)
    }
}