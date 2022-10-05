package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.domain.Film
import com.pisakov.skillproj.domain.Interactor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeFragmentViewModel : ViewModel(), KoinComponent {
    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    private val interactor: Interactor by inject()

    private var page = 1

    init {
        loadNewPage()
    }

    fun loadNewPage() {
        interactor.getFilmsFromApi(page, object : ApiCallback {
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


    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}