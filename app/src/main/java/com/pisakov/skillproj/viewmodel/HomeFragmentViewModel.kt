package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.*
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.ApiCallback
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    var updatingUIState: BehaviorSubject<Boolean> = BehaviorSubject.create()

    var filmsDataBase = mutableListOf<Film>()

    private var page = 1

    init {
        App.instance.dagger.inject(this)
        loadNewPage()
        registerSharedPrefListener()
    }

    fun loadNewPage() {
        progressBarState.onNext(true)
        interactor.getFilmsFromApi(page, interactor.getDefaultCategoryFromPreferences(), object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsDataBase = (filmsDataBase + films) as MutableList<Film>
                updatingUIState.onNext(true)
                progressBarState.onNext(false)
            }
            override fun onFailure() {
                interactor.getFilmsFromDB()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { list ->
                        filmsDataBase = list as MutableList<Film>
                        updatingUIState.onNext(true)
                    }
                progressBarState.onNext(false)
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
        updatingUIState.onNext(true)
    }

    fun resetUpdatingState() {
        updatingUIState.onNext(false)
    }
}