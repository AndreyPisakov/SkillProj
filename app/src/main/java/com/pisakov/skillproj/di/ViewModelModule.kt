package com.pisakov.skillproj.di

import com.pisakov.skillproj.BuildConfig
import com.pisakov.skillproj.data.ApiConstants
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.data.TmdbApi
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.viewmodel.HomeFragmentViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {
    @Provides
    fun provideRepo(): MainRepository = MainRepository()

    @Provides
    fun provideRetrofitService(): TmdbApi {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(TmdbApi::class.java)
    }

    @Provides
    fun provideInteractor(repo: MainRepository, retrofitService: TmdbApi): Interactor = Interactor(repo, retrofitService)

    @Provides
    fun provideViewModel(interactor: Interactor): HomeFragmentViewModel = HomeFragmentViewModel(interactor)
}