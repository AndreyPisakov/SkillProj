package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchFragmentViewModel : ViewModel()  {
    @Inject
    lateinit var interactor: Interactor

    var filmsDataBase = mutableListOf<Film>()
    var pageQuery = 1

    init {
        App.instance.dagger.inject(this)
    }

    fun loadNewPage(query: String): Observable<List<Film>> {
        pageQuery++
        return interactor.getFilmsFromQueryApi(pageQuery, query)
    }
}