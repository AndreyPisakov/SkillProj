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
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.data.entity.Notification
import com.pisakov.skillproj.databinding.NotificationItemBinding

class NotificationListRecyclerAdapter(
    private val edit: (item: Pair<Notification, Film>) -> Unit,
    private val delete: (film: Film) -> Unit,
    private val click: (film: Film) -> Unit):
    ListAdapter<Pair<Notification, Film>, NotificationListRecyclerAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false))

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.first, item.second)
        holder.click(edit, delete, click, item)
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = NotificationItemBinding.bind(itemView)

        fun bind(notification: Notification, film: Film) {
            binding.apply {
                title.text = film.title
                val dateTime: String
                notification.apply {
                    val month = if (currentMonth + 1 >= 10) (currentMonth + 1).toString() else "0${currentMonth + 1}"
                    val day = if (currentDay >= 10) currentDay.toString() else "0$currentDay"
                    val hour = if (currentHour >= 10) currentHour.toString() else "0$currentHour"
                    val minute = if (currentMinute >= 10) currentMinute.toString() else "0$currentMinute"

                    dateTime = "$day.$month.$currentYear  $hour:$minute"
                }
                dateAndTime.text = dateTime
                ratingView.setProgress(film.rating.toFloat())
                Glide.with(itemView)
                    .load(ApiConstants.IMAGES_URL + "w342" + film.poster)
                    .error(R.drawable.error)
                    .centerCrop()
                    .into(poster)
            }
        }

        fun click(edit: (item: Pair<Notification, Film>) -> Unit,
                  delete: (film: Film) -> Unit,
                  click: (film: Film) -> Unit,
                  item: Pair<Notification, Film>) {
            binding.btnEdit.setOnClickListener { edit(item) }
            binding.btnDelete.setOnClickListener { delete(item.second) }
            binding.poster.setOnClickListener { click(item.second) }
        }
    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<Pair<Notification, Film>>() {
        override fun areItemsTheSame(oldItem: Pair<Notification, Film>, newItem: Pair<Notification, Film>): Boolean =
                    oldItem.first.currentYear == newItem.first.currentYear &&
                    oldItem.first.currentMonth == newItem.first.currentMonth &&
                    oldItem.first.currentDay == newItem.first.currentDay &&
                    oldItem.first.currentHour == newItem.first.currentHour &&
                    oldItem.first.currentMinute == newItem.first.currentMinute
        override fun areContentsTheSame(oldItem: Pair<Notification, Film>, newItem: Pair<Notification, Film>): Boolean = oldItem == newItem
    }
}