package com.xiao.today.basicdraw.layout.viewpage

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.xiao.today.basicdraw.layout.viewpage.placeholder.PlaceholderContent.PlaceholderItem
import com.xiao.today.basicdraw.databinding.FragmentItemBinding

class ItemRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}