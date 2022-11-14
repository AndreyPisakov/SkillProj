package com.pisakov.skillproj.view.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pisakov.skillproj.R
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.databinding.SelectionItemBinding

class SelectionListRecyclerAdapter(
    private val list: List<String>,
    private val click: (title: String) -> Unit) : RecyclerView.Adapter<SelectionListRecyclerAdapter.SelectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder =
        SelectionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.selection_item, parent, false))

    override fun onBindViewHolder(holder: SelectionViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.binding.itemContainer.setOnClickListener {
            click(item)
        }
    }

    class SelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SelectionItemBinding.bind(itemView)

        fun bind(title: String) {
            binding.selectionName.text = title
        }
    }

    override fun getItemCount(): Int = list.size
}