package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.Selections
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class ListFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    private lateinit var disposable: Disposable

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    var updatingUIState: BehaviorSubject<Boolean> = BehaviorSubject.create()

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

    fun loadList(){
        progressBarState.onNext(true)
        interactor.getFilmsFromApi(page, selectionName)
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
                    interactor.loadFilmsToDB(it, selectionName)
                }
            )
        page++
    }

    fun resetUpdatingState() {
        updatingUIState.onNext(false)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}