package com.gumibom.travelmaker.ui.main.findmate.meeting_post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.databinding.ItemMeetingPostPictureBinding

class MeetingPostAdapter : ListAdapter<String, MeetingPostAdapter.MeetingPostViewHolder>(MeetingPostDiffUtil()) {

    class MeetingPostViewHolder(private val binding : ItemMeetingPostPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(urlString : String) {
            Glide.with(binding.ivMeetingPostPicture)
                .load(urlString)
                .into(binding.ivMeetingPostPicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingPostViewHolder {
        val binding = ItemMeetingPostPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeetingPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeetingPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}