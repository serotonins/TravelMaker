package com.gumibom.travelmaker.ui.main.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.data.dto.request.SentRequest
import com.gumibom.travelmaker.databinding.ItemFcmNotifyRequireListBinding
import com.gumibom.travelmaker.ui.main.MainViewModel

class MainFcmNotifySentAdapter
    (private val context:Context, private val viewModel: MainViewModel)
    :ListAdapter<SentRequest,MainFcmNotifySentAdapter.SentViewHolder>(MainNotifySentDiffUtil()){


    inner class SentViewHolder(private val binding : ItemFcmNotifyRequireListBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(item:SentRequest){
            with(binding){
                Glide.with(context).load(item.meetingPostMainImg).into(imgRequirePlaceItem)
                tvRequestTitleItem.text = item.meetingPostTitle
                tvRequestDeadlineItem.text =item.meetingPostDeadline
                tvHeadNameItem.text = item.acceptorName
                btnCancelCrew.setOnClickListener {

                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainFcmNotifySentAdapter.SentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFcmNotifyRequireListBinding.inflate(layoutInflater,parent,false)
        return SentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainFcmNotifySentAdapter.SentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}