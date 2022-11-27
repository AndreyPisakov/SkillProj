package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.*
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.ApiCallback
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    val progressBarStateFlow = MutableStateFlow(false)
    val updatingUIStateFlow = MutableStateFlow(false)

    var filmsDataBase = mutableListOf<Film>()

    private var page = 1

    init {
        App.instance.dagger.inject(this)
        loadNewPage()
        registerSharedPrefListener()
    }

    fun loadNewPage() {
        progressBarStateFlow.value = true
        interactor.getFilmsFromApi(page, interactor.getDefaultCategoryFromPreferences(), object : ApiCallback {
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
        })
        page++
    }

    private fun registerSharedPrefListener(){
        interactor.registerSharedPrefListener(
            change = {
                clearListFilms()
                page = 1
                loadNewPage()
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        interactor.unregisterSharedPrefListener()
    }

    private fun clearListFilms() {
        filmsDataBase.clear()
        updatingUIStateFlow.value = true
    }

    fun resetUpdatingState() {
        updatingUIStateFlow.value = false
    }
}