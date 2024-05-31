package com.gumibom.travelmaker.ui.main.myrecord.detail

import androidx.recyclerview.widget.DiffUtil
import com.gumibom.travelmaker.model.pamphlet.Record

class MyRecordDetailDiffUtil : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.recordId == newItem.recordId
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}