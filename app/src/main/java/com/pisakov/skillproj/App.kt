package com.pisakov.skillproj

import android.app.Application
import com.pisakov.skillproj.data.ApiConstants
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.data.TmdbApi
import com.pisakov.skillproj.di.AppComponent
import com.pisakov.skillproj.di.DaggerAppComponent
import com.pisakov.skillproj.domain.Interactor
import dagger.internal.DaggerGenerated
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.create()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}