package com.gumibom.travelmaker.ui.main.notification

import androidx.recyclerview.widget.DiffUtil
import com.gumibom.travelmaker.data.dto.request.SentRequest

class MainNotifySentDiffUtil: DiffUtil.ItemCallback<SentRequest>() {
    override fun areItemsTheSame(oldItem: SentRequest, newItem: SentRequest): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SentRequest, newItem: SentRequest): Boolean {
        return oldItem.requestId == newItem.requestId
    }


}