package com.pisakov.skillproj

import android.app.Application
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.domain.Interactor

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var interactor: Interactor

    override fun onCreate() {
        super.onCreate()
        instance = this
        repo = MainRepository()
        interactor = Interactor(repo)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}