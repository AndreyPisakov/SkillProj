package com.pisakov.skillproj.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notifications", foreignKeys = [ForeignKey(
    entity = Film::class,
    parentColumns = ["id"],
    childColumns = ["notification_id"],
    onDelete = ForeignKey.CASCADE)])
data class Notification(
    @PrimaryKey @ColumnInfo(name = "notification_id") val id: Int,
    @ColumnInfo(name = "current_year") val currentYear: Int,
    @ColumnInfo(name = "current_month") val currentMonth: Int,
    @ColumnInfo(name = "current_day") val currentDay: Int,
    @ColumnInfo(name = "current_hour") val currentHour: Int,
    @ColumnInfo(name = "current_minute") val currentMinute: Int
): Parcelable
