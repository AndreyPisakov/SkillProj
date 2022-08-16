package com.pisakov.skillproj

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Film(
    val filmId: Int,
    val title: String,
    val poster: Int,
    val rating: Float,
    val description: String,
    var isInFavorites: Boolean = false
) : Parcelable {

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readString().toString(),
        parcel.readBoolean()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(filmId)
        p0?.writeString(title)
        p0?.writeInt(poster)
        p0?.writeFloat(rating)
        p0?.writeString(description)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            p0?.writeBoolean(isInFavorites)
    }

    companion object CREATOR : Parcelable.Creator<Film> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Film {
            return Film(parcel)
        }

        override fun newArray(size: Int): Array<Film?> {
            return arrayOfNulls(size)
        }
    }
}