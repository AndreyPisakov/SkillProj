package com.pisakov.skillproj.view.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pisakov.remote_module.entity.ApiConstants
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FilmItemBinding
import com.pisakov.skillproj.data.entity.Film

class FilmListRecyclerAdapter(private val click: (film: Film) -> Unit, private val loadNewPage: () -> Unit):
    ListAdapter<Film, FilmListRecyclerAdapter.FilmViewHolder>(FilmsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder =
        FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false))

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        if (position == itemCount - 3)
            loadNewPage()
        val item = getItem(position)
        holder.bind(item)
        holder.binding.itemContainer.setOnClickListener {
            click(item)
        }
    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FilmItemBinding.bind(itemView)

        fun bind(film: Film) {
            binding.apply {
                title.text = film.title
                description.text = film.description
                ratingView.setProgress(film.rating.toFloat())
                Glide.with(itemView)
                    .load(ApiConstants.IMAGES_URL + "w342" + film.poster)
                    .error(R.drawable.error)
                    .centerCrop()
                    .into(poster)
            }
        }
    }

    class FilmsDiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem == newItem
    }
}