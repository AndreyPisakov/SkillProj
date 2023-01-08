package com.pisakov.skillproj.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.pisakov.skillproj.App
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.domain.Interactor
import com.pisakov.skillproj.notifications.NotificationConstants
import com.pisakov.skillproj.notifications.NotificationHelper
import javax.inject.Inject

class ReminderBroadcast: BroadcastReceiver() {
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val film: Film? = intent?.getParcelableExtra(NotificationConstants.BUNDLE_KEY)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            NotificationHelper.createNotification(context!!, film)
            film?.let { interactor.deleteNotification(it.id) }
        }
    }
}