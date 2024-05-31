package com.gumibom.travelmaker.ui.main.findmate.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gumibom.travelmaker.databinding.ItemFindmateBtsChipBinding

class chipAdapter(private val chipLists: List<String>) :
    RecyclerView.Adapter<chipAdapter.ChipViewHolder>()
{
    class ChipViewHolder(val binding:ItemFindmateBtsChipBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemFindmateBtsChipBinding.
        inflate(LayoutInflater.from(parent.context),parent,false)
        return ChipViewHolder(binding)
    }
    override fun onBindViewHolder(holder:ChipViewHolder, position: Int) {
     val chipCategoryName = chipLists[position]
        holder.binding.chipCategoryItem.text = chipCategoryName.toString()
        //색깔?
    }

    override fun getItemCount(): Int {
        return chipLists.size
    }
}