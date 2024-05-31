package com.gumibom.travelmaker.ui.main.lookpamphlets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gumibom.travelmaker.databinding.ItemCategoryBinding

class CategoryAdapter : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(CategoryDiffUtil()) {

    class CategoryViewHolder(private val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            binding.tvItemCategory.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}