package com.pisakov.skillproj

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pisakov.skillproj.databinding.FilmItemBinding

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = FilmItemBinding.bind(itemView)

    fun bind(film: Film) {
        binding.apply {
            title.text = film.title
            poster.setImageResource(film.poster)
            description.text = film.description
        }
    }
}