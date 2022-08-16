package com.pisakov.skillproj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pisakov.skillproj.databinding.FilmItemBinding

class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) : ListAdapter<Film, FilmListRecyclerAdapter.FilmViewHolder>(FilmsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder =
        FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false))

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.binding.itemContainer.setOnClickListener {
            clickListener.click(item)
        }
    }

    interface OnItemClickListener {
        fun click(film: Film)
    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FilmItemBinding.bind(itemView)

        fun bind(film: Film) {
            binding.apply {
                title.text = film.title
                description.text = film.description
                ratingView.setProgress(film.rating)
                Glide.with(itemView)
                    .load(film.poster)
                    .centerCrop()
                    .into(poster)
            }
        }
    }

    class FilmsDiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem.filmId == newItem.filmId
        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem == newItem
    }
}