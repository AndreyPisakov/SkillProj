package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.Selections

class HomeFragmentViewModel : ViewModel() {
    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    private var interactor: Interactor = App.instance.interactor

    private var page = 1

    init {
        loadNewPage()
    }

    fun loadNewPage() {
        interactor.getListFilmsFromApi(Selections.popular, page, object : Interactor.ApiCallback {
            override fun onSuccess(films: List<Film>) {
                val list = mutableListOf<Film>()
                _filmListLiveData.value?.let { list.addAll(it) }
                list.addAll(films)
                _filmListLiveData.postValue(list)
            }
            override fun onFailure() {}
        })
        page++
    }

}