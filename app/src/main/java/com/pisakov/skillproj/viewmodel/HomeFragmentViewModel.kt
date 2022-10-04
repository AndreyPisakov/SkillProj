package com.pisakov.skillproj.viewmodel

import android.util.Log
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

//    init {
//        val films = interactor.getFilmsDB()
//        _filmListLiveData.postValue(films)
//    }

    init {
        Log.d("MyLog", "vm init")
        interactor.getFilmsFromApi(1, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                _filmListLiveData.postValue(films)
                Log.d("MyLog", "success")
            }

            override fun onFailure() {
                Log.d("MyLog", "error")

            }
        })
    }

    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}