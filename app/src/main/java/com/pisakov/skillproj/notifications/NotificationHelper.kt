package com.pisakov.skillproj.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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
import com.pisakov.skillproj.view.MainActivity

object NotificationHelper {
    @RequiresApi(Build.VERSION_CODES.S)
    fun createNotification(context: Context, film: Film) {
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
}