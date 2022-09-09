package com.pisakov.skillproj.data

import com.pisakov.skillproj.R
import com.pisakov.skillproj.domain.Film

class MainRepository {
    val filmsDataBase = listOf(
        Film(1,"Star is born", R.drawable.poster_1, 2.2f, "This should be a description", true),
        Film(2,"Kill Bill", R.drawable.poster_2, 9.9f,"This should be a description", true),
        Film(3,"Bring him home", R.drawable.poster_3, 4.7f,"This should be a description", true),
        Film(4,"Hard candy", R.drawable.poster_4, 6.3f,"This should be a description"),
        Film(5,"John Wick", R.drawable.poster_5, 8.5f, "This should be a description"),
        Film(6,"Фото на память", R.drawable.poster_6, 7.7f, "This should be a description"),
        Film(7,"Color out of space", R.drawable.poster_7, 9.1f, "This should be a description"),
        Film(8,"Маша", R.drawable.poster_8, 5.6f, "This should be a description")    )
}