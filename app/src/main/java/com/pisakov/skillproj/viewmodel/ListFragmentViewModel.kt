package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.ApiCallback
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.Selections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    val progressBarStateFlow = MutableStateFlow(false)
    val updatingUIStateFlow = MutableStateFlow(false)

    var filmsDataBase = mutableListOf<Film>()

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
            viewModelScope.launch {
                filmsDataBase = (filmsDataBase + films) as MutableList<Film>
                updatingUIStateFlow.value = true
            }
            progressBarStateFlow.value = false
        }
        override fun onFailure() {
            viewModelScope.launch {
                interactor.getFilmsFromDB().collect {
                    filmsDataBase = it as MutableList<Film>
                    updatingUIStateFlow.value = true
                }
            }
            progressBarStateFlow.value = false
        }
    }

    fun loadList(){
        progressBarStateFlow.value = true
        interactor.getFilmsFromApi(page, selectionName, callback)
        page++
    }

    fun resetUpdatingState() {
        updatingUIStateFlow.value = false
    }
}