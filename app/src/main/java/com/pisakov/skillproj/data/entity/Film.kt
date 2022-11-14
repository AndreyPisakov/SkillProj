package com.pisakov.skillproj.data.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cached_films")
data class Film(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "overview") val description: String,
    @ColumnInfo(name = "vote_average") var rating: Double = 0.0,
    @ColumnInfo(name = "is_in_favorites") var isInFavorites: Boolean = false
) : Parcelable



@Entity(tableName = "category", foreignKeys = [ForeignKey(
    entity = Film::class,
    parentColumns = ["id"],
    childColumns = ["film_id"],
    onDelete = ForeignKey.CASCADE)])
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "film_id") val filmId: Int = 0,
    @ColumnInfo(name = "is_popular") var isPopular: Boolean = false,
    @ColumnInfo(name = "is_top_rated") var isTopRated: Boolean = false,
    @ColumnInfo(name = "is_now_playing") var isNowPlaying: Boolean = false,
    @ColumnInfo(name = "is_upcoming") var isUpcoming: Boolean = false
)