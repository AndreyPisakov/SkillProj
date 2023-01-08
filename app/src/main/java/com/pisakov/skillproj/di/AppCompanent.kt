package com.pisakov.skillproj.di

import com.pisakov.remote_module.RemoteProvider
import com.pisakov.skillproj.di.module.DatabaseModule
import com.pisakov.skillproj.di.module.DomainModule
import com.pisakov.skillproj.receivers.ReminderBroadcast
import com.pisakov.skillproj.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RemoteProvider::class],
    modules = [
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
    fun inject(searchFragmentViewModel: SearchFragmentViewModel)
    fun inject(laterFragmentViewModel: LaterFragmentViewModel)
    fun inject(reminderBroadcast: ReminderBroadcast)
}