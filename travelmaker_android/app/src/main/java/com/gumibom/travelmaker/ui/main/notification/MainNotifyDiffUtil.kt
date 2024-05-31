package com.gumibom.travelmaker.ui.main.notification

import androidx.recyclerview.widget.DiffUtil
import com.gumibom.travelmaker.data.dto.request.ReceivedRequest
import com.gumibom.travelmaker.model.NotifyReceiveItem

class MainNotifyDiffUtil  : DiffUtil.ItemCallback<ReceivedRequest>() {

    override fun areItemsTheSame(oldItem: ReceivedRequest, newItem: ReceivedRequest): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ReceivedRequest, newItem: ReceivedRequest): Boolean {
        return oldItem.meetingPostId == newItem.meetingPostId
    }

}