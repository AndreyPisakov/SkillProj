package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.ApiCallback
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.AutoDisposable
import com.pisakov.skillproj.utils.Selections
import com.pisakov.skillproj.utils.addTo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class ListFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    private val autoDisposable = AutoDisposable()

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

    private val callback = object : ApiCallback {
        override fun onSuccess(films: List<Film>) {
            filmsDataBase = (filmsDataBase + films) as MutableList<Film>
            updatingUIState.onNext(true)
            progressBarState.onNext(false)
        }
        override fun onFailure() {
            interactor.getFilmsFromDB()
                .subscribe { list ->
                    filmsDataBase = list as MutableList<Film>
                    updatingUIState.onNext(true)
                }.addTo(autoDisposable)
            progressBarState.onNext(false)
        }
    }

    fun loadList(){
        progressBarState.onNext(true)
        interactor.getFilmsFromApi(page, selectionName, callback)
        page++
    }

    fun resetUpdatingState() {
        updatingUIState.onNext(false)
    }
}