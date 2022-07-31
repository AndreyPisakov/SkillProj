package com.pisakov.skillproj

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pisakov.skillproj.databinding.FilmItemBinding

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = FilmItemBinding.bind(itemView)

    fun bind(film: Film) {
        binding.apply {
            title.text = film.title
            description.text = film.description
            Glide.with(itemView)
                .load(film.poster)
                .centerCrop()
                .into(poster)
        }
    }
}