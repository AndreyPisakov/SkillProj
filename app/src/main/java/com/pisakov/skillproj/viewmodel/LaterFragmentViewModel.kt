package com.pisakov.skillproj.viewmodel

import androidx.lifecycle.ViewModel
import com.pisakov.skillproj.App
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.data.entity.Notification
import com.pisakov.skillproj.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LaterFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun editNotification(notification: Notification) = interactor.editNotification(notification)
    fun deleteNotification(id: Int) = interactor.deleteNotification(id)

    fun getNotificationList(): Observable<List<Notification>> = interactor.getNotificationList()
    fun getFilmsFromId(id: List<Int>): Observable<List<Film>> = interactor.getFilmsFromId(id)
}