package com.gumibom.travelmaker.ui.main.findmate.meeting_post

import androidx.recyclerview.widget.DiffUtil

class MeetingPostDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}