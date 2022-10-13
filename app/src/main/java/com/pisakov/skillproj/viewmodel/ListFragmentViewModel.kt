package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.Selections

class ListFragmentViewModel : ViewModel() {
    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    private var interactor: Interactor = App.instance.interactor

    var page: Int = 1

    private val callback = object : Interactor.ApiCallback {
        override fun onSuccess(films: List<Film>) {
            val list = mutableListOf<Film>()
            _filmListLiveData.value?.let { list.addAll(it) }
            list.addAll(films)
            _filmListLiveData.postValue(list)
        }
        override fun onFailure() {}
    }

    fun loadList(selectionName: String){
        when(selectionName) {
            Selections.popular -> {
                interactor.getListFilmsFromApi(selectionName, page, callback)
                page++
            }
            Selections.latest -> {
                interactor.getListFilmsFromApi(selectionName, page, callback)
            }
            Selections.now_playing -> {
                interactor.getListFilmsFromApi(selectionName, page, callback)
                page++
            }
            Selections.top_rated -> {
                interactor.getListFilmsFromApi(selectionName, page, callback)
                page++
            }
            Selections.upcoming -> {
                interactor.getListFilmsFromApi(selectionName, page, callback)
                page++
            }
        }
    }
}