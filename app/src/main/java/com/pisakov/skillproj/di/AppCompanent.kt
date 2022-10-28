package com.pisakov.skillproj.di

import com.pisakov.skillproj.di.module.DatabaseModule
import com.pisakov.skillproj.di.module.DomainModule
import com.pisakov.skillproj.di.module.RemoteModule
import com.pisakov.skillproj.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    fun inject(listFragmentViewModel: ListFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
    fun inject(favoriteFragmentViewModel: FavoriteFragmentViewModel)
    fun inject(detailsFragmentViewModel: DetailsFragmentViewModel)
}