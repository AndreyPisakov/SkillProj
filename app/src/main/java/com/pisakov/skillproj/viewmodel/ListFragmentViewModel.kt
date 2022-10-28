package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.ApiCallback
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.Selections
import javax.inject.Inject

class ListFragmentViewModel : ViewModel() {
    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    @Inject
    lateinit var interactor: Interactor

    var page: Int = 1

    init {
        App.instance.dagger.inject(this)
    }

    private val callback = object : ApiCallback {
        override fun onSuccess(films: List<Film>) {
            val list = mutableListOf<Film>()
            _filmListLiveData.value?.let { list.addAll(it) }
            list.addAll(films)
            _filmListLiveData.postValue(list)
        }
        override fun onFailure() {}
    }

    fun loadList(selectionName: String){
        when (selectionName){
            Selections.R_POPULAR_CATEGORY -> interactor.getFilmsFromApi(page, Selections.POPULAR_CATEGORY, callback)
            Selections.R_TOP_RATED_CATEGORY -> interactor.getFilmsFromApi(page, Selections.TOP_RATED_CATEGORY, callback)
            Selections.R_NOW_PLAYING_CATEGORY -> interactor.getFilmsFromApi(page, Selections.NOW_PLAYING_CATEGORY, callback)
            Selections.R_UPCOMING_CATEGORY -> interactor.getFilmsFromApi(page, Selections.UPCOMING_CATEGORY, callback)
        }
        page++
    }
}