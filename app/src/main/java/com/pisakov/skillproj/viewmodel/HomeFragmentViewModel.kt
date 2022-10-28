package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.ApiCallback
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import java.util.concurrent.Executors
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    @Inject
    lateinit var interactor: Interactor

    private var page = 1

    init {
        App.instance.dagger.inject(this)
        loadNewPage()
        registerSharedPrefListener()
    }

    fun loadNewPage() {
        interactor.getFilmsFromApi(page, interactor.getDefaultCategoryFromPreferences(), object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                val list = mutableListOf<Film>()
                _filmListLiveData.value?.let { list.addAll(it) }
                list.addAll(films)
                _filmListLiveData.postValue(list)
            }
            override fun onFailure() {
                Executors.newSingleThreadExecutor().execute {
                    _filmListLiveData.postValue(interactor.getFilmsFromDB())
                }
            }
        })
        page++
    }

    private fun registerSharedPrefListener(){
        interactor.registerSharedPrefListener(object : Interactor.onSharedPrefChange {
            override fun change() {
                page = 1
                _filmListLiveData.value = listOf()
                loadNewPage()
            }
        })
    }
}