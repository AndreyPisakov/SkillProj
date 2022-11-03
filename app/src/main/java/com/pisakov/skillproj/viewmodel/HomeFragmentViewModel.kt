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
    @Inject
    lateinit var interactor: Interactor

    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private var page = 1

    init {
        App.instance.dagger.inject(this)
        loadNewPage()
        registerSharedPrefListener()
    }

    fun loadNewPage() {
        _showProgressBar.value = true
        interactor.getFilmsFromApi(page, interactor.getDefaultCategoryFromPreferences(), object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                val list = mutableListOf<Film>()
                _filmListLiveData.value?.let { list.addAll(it) }
                list.addAll(films)
                _filmListLiveData.value = list

                _showProgressBar.value = false
            }
            override fun onFailure() {
                Executors.newSingleThreadExecutor().execute { _filmListLiveData.postValue(interactor.getFilmsFromDB()) }
                _showProgressBar.value = false
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

    override fun onCleared() {
        super.onCleared()
        interactor.unregisterSharedPrefListener()
    }
}