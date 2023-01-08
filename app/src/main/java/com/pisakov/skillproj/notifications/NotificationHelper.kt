package com.pisakov.skillproj.notifications

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pisakov.remote_module.entity.ApiConstants
import com.pisakov.skillproj.R
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.data.entity.Notification
import com.pisakov.skillproj.receivers.ReminderBroadcast
import com.pisakov.skillproj.view.MainActivity
import io.reactivex.rxjava3.core.Observable
import java.util.*

object NotificationHelper {
    fun createNotification(context: Context, film: Film?) {
        if (film == null)
            return
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.detailsFragment, bundleOf(Pair(NotificationConstants.BUNDLE_KEY, film)))
            .createPendingIntent()
        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_watch_later)
            setContentTitle(context.getString(R.string.notification_wathc_later))
            setContentText(film.title)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        Glide.with(context)
            .asBitmap()
            .load(ApiConstants.IMAGES_URL + "w500" + film.poster)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    notificationManager.notify(film.id, builder.build())
                }
            })
        notificationManager.notify(film.id, builder.build())
    }

    fun notificationSet(context: Context, film: Film): Observable<Notification> {
        val calendar = Calendar.getInstance()

        return Observable.create {
            DatePickerDialog(
                context,
                { _, dpdYear, dpdMonth, dayOfMonth ->
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, pickerMinute ->
                            val pickedDateTime = Calendar.getInstance()
                            pickedDateTime.set(
                                dpdYear,
                                dpdMonth,
                                dayOfMonth,
                                hourOfDay,
                                pickerMinute,
                                0
                            )
                            val dateTimeInMillis = pickedDateTime.timeInMillis
                            createWatchLaterEvent(context, dateTimeInMillis, film)
                            it.onNext(Notification(film.id, dpdYear, dpdMonth, dayOfMonth, hourOfDay, pickerMinute))
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun createWatchLaterEvent(context: Context, dateTimeInMillis: Long, film: Film) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(film.title, null, context, ReminderBroadcast()::class.java)
        intent.putExtra(NotificationConstants.BUNDLE_KEY, film)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            dateTimeInMillis,
            pendingIntent
        )
    }

    fun cancelWatchLaterEvent(context: Context, film: Film) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(film.title, null, context, ReminderBroadcast()::class.java)
        intent.putExtra(NotificationConstants.BUNDLE_KEY, film)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}