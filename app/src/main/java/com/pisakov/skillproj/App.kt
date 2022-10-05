package com.pisakov.skillproj

import android.app.Application
import com.pisakov.skillproj.data.ApiConstants
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.data.TmdbApi
import com.pisakov.skillproj.di.DI
import com.pisakov.skillproj.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(listOf(DI.mainModule))
        }
    }
}