package com.pisakov.skillproj

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.pisakov.remote_module.DaggerRemoteComponent
import com.pisakov.skillproj.di.AppComponent
import com.pisakov.skillproj.di.DaggerAppComponent
import com.pisakov.skillproj.di.module.DatabaseModule
import com.pisakov.skillproj.di.module.DomainModule
import com.pisakov.skillproj.notifications.NotificationConstants.CHANNEL_ID

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        val remoteProvider = DaggerRemoteComponent.create()
        dagger = DaggerAppComponent.builder()
            .remoteProvider(remoteProvider)
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, CHANNAL_NAME, importance)
            mChannel.description = CHANNAL_DESCRIPTION
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        private const val CHANNAL_NAME = "WatchLaterChannel"
        private const val CHANNAL_DESCRIPTION = "FilmsSearch notification Channel"

        lateinit var instance: App
            private set
    }
}

