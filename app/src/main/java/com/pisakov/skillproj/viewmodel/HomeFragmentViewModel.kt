package com.pisakov.skillproj.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.domain.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.utils.Selections
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    private val _filmListLiveData = MutableLiveData<List<Film>>()
    val filmListLiveData: LiveData<List<Film>>
        get() = _filmListLiveData

    @Inject
    lateinit var interactor: Interactor

    private var page = 1
    private var categoryString: String = ""

    init {
        App.instance.dagger.inject(this)
        loadNewPage()
        categoryString = getMainCategory()
    }

    fun loadNewPage(category: String = categoryString) {
        interactor.getListFilmsFromApi(page, category, object : Interactor.ApiCallback {
            override fun onSuccess(films: List<Film>) {
                val list = mutableListOf<Film>()
                _filmListLiveData.value?.let { list.addAll(it) }
                list.addAll(films)
                _filmListLiveData.postValue(list)
            }
            override fun onFailure() {}
        })
        page++
    }

    fun cleanList(){
        _filmListLiveData.value = listOf()
        page = 1
    }

    fun getMainCategory(): String = interactor.getDefaultCategoryFromPreferences()

    fun getMainCategoryInt(): Int {
        Log.d("MyLog", "setCurrentCategory")
        return when (getMainCategory()){
            Selections.POPULAR_CATEGORY -> 0
            Selections.TOP_RATED_CATEGORY -> 1
            Selections.LATEST_CATEGORY -> 4
            Selections.UPCOMING_CATEGORY -> 2
            Selections.NOW_PLAYING_CATEGORY -> 3
            else -> 0
        }
    }

    fun getChoosingCategory(numberCategory: Int): String {
        categoryString = when (numberCategory){
            0 -> Selections.POPULAR_CATEGORY
            1 -> Selections.TOP_RATED_CATEGORY
            2 -> Selections.UPCOMING_CATEGORY
            3 -> Selections.NOW_PLAYING_CATEGORY
            4 -> Selections.LATEST_CATEGORY
            else -> getMainCategory()
        }
        return categoryString
    }
}