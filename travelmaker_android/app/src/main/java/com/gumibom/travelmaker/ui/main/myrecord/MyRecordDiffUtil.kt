package com.gumibom.travelmaker.ui.main.myrecord

import androidx.recyclerview.widget.DiffUtil
import com.gumibom.travelmaker.model.pamphlet.PamphletItem

class MyRecordDiffUtil : DiffUtil.ItemCallback<PamphletItem>() {
    override fun areItemsTheSame(oldItem: PamphletItem, newItem: PamphletItem): Boolean {
        return oldItem.pamphletId == newItem.pamphletId
    }

    override fun areContentsTheSame(oldItem: PamphletItem, newItem: PamphletItem): Boolean {
        return oldItem == newItem
    }
}