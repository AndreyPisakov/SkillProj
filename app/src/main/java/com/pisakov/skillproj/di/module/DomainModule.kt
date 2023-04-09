package com.pisakov.skillproj.di.module

import android.content.Context
import com.pisakov.remote_module.TmdbApi
import com.pisakov.skillproj.data.MainRepository
import com.pisakov.skillproj.data.PreferenceProvider
import com.pisakov.skillproj.domain.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: TmdbApi, preferenceProvider: PreferenceProvider) =
        Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)
}