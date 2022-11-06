package com.example.myapplication.ui.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.databinding.ItemMarkersListBinding
import com.example.myapplication.domain.entity.Marker

class MarkersViewAdapter(
    val onClickListener: (Marker) -> Unit
) : RecyclerView.Adapter<MarkersViewAdapter.ViewHolder>() {

    private val items: MutableList<Marker> = mutableListOf()

    fun submitList(newItems: List<Marker>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMarkersListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(binding: ItemMarkersListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val contentView: TextView = binding.content

        fun bind(marker: Marker) {
            contentView.text = marker.title
            itemView.setOnClickListener {
                onClickListener.invoke(marker)
            }
        }
    }

}