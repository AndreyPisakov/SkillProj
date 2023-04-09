package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.*
import com.pisakov.skillproj.App
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    var updatingUIState: BehaviorSubject<Boolean> = BehaviorSubject.create()

    var filmsDataBase = mutableListOf<Film>()

    private lateinit var disposable: Disposable

    private var page = 1


    init {
        App.instance.dagger.inject(this)
        loadNewPage()
        registerSharedPrefListener()
    }

    fun loadNewPage() {
        progressBarState.onNext(true)
        interactor.getFilmsFromApi(page, interactor.getDefaultCategoryFromPreferences())
            .subscribeBy(
                onError = {
                    disposable = interactor.getFilmsFromDB()
                    .subscribe { list ->
                        filmsDataBase = list as MutableList<Film>
                        updatingUIState.onNext(true)
                    }
                    progressBarState.onNext(false)
                },
                onNext = {
                    filmsDataBase = (filmsDataBase + it) as MutableList<Film>
                    progressBarState.onNext(false)
                    updatingUIState.onNext(true)
                    interactor.loadFilmsToDB(it, interactor.getDefaultCategoryFromPreferences())
                }
            )
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
        disposable.dispose()
    }

    private fun clearListFilms() {
        filmsDataBase.clear()
        updatingUIState.onNext(true)
    }

    fun resetUpdatingState() {
        updatingUIState.onNext(false)
    }
}