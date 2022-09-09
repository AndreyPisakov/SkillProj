package com.pisakov.skillproj

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val filmId: Int,
    val title: String,
    val poster: Int,
    val rating: Float,
    val description: String,
    var isInFavorites: Boolean = false
) : Parcelable