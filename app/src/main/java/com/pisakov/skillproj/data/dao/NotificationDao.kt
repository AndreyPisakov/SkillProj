package com.pisakov.skillproj.data.dao

import androidx.room.*
import com.pisakov.skillproj.data.entity.Notification
import io.reactivex.rxjava3.core.Observable

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: Notification)

    @Query("DELETE FROM notifications WHERE notification_id = :id")
    fun deleteNotification(id: Int)

    @Update
    fun updateNotification(notification: Notification)

    @Query("SELECT * FROM notifications")
    fun getAllNotifications(): Observable<List<Notification>>
}