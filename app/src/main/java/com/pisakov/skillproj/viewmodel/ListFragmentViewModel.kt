package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.ApiCallback
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.Selections
import java.util.concurrent.Executors
import javax.inject.Inject

class ListFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    var page: Int = 1
    var selectionName = ""
        set(value) {
            field = when (value){
                Selections.R_POPULAR_CATEGORY -> Selections.POPULAR_CATEGORY
                Selections.R_TOP_RATED_CATEGORY -> Selections.TOP_RATED_CATEGORY
                Selections.R_NOW_PLAYING_CATEGORY -> Selections.NOW_PLAYING_CATEGORY
                Selections.R_UPCOMING_CATEGORY -> Selections.UPCOMING_CATEGORY
                else -> return
            }
        }

    init {
        App.instance.dagger.inject(this)
    }

    private val callback = object : ApiCallback {
        override fun onSuccess(films: List<Film>) {
            val list = mutableListOf<Film>()
            _filmListLiveData.value?.let { list.addAll(it) }
            list.addAll(films)
            _filmListLiveData.postValue(list)
            _showProgressBar.value = false
        }
        override fun onFailure() {
            Executors.newSingleThreadExecutor().execute { _filmListLiveData.postValue(interactor.getFilmsFromDB(selectionName)) }
            _showProgressBar.value = false
        }
    }

    fun loadList(){
        _showProgressBar.value = true
        interactor.getFilmsFromApi(page, selectionName, callback)
        page++
    }
}