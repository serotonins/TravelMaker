package com.gumibom.travelmaker.ui.main.myrecord

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.ItemMyRecordBinding
import com.gumibom.travelmaker.model.pamphlet.PamphletItem

private const val TAG = "MyRecordAdapter_싸피"
class MyRecordAdapter(private val context : Context, private val myRecordViewModel: MyRecordViewModel) : ListAdapter<PamphletItem, MyRecordAdapter.MyRecordViewHolder>(MyRecordDiffUtil()) {
    var itemClickListener: ItemClickListener? = null
    inner class MyRecordViewHolder(private val binding : ItemMyRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : PamphletItem) {
            // 여행 중인 경우
            if (!item.isFinish) {
                setItem(item)

                binding.tvItemMyRecordCreateTime.visibility = View.VISIBLE
                binding.btnMyRecordComplte.visibility = View.VISIBLE
            }
            // 여행 완료 인 경우
            else {
                setItem(item)

                binding.tvItemMyRecordCreateTime.visibility = View.VISIBLE
                binding.btnMyRecordComplte.visibility = View.GONE
            }

            binding.btnMyRecordComplte.setOnClickListener {
                myRecordViewModel.finishTravelPamphlet(item.pamphletId, item)
            }

            binding.ivMyRecordPamphlet.setOnClickListener {
                moveDetailFragment(item.pamphletId)
            }
        }

        private fun setItem(item : PamphletItem){
            binding.tvMyRecordTitle.text = item.title

            val imageUrl = item.repreImgUrl
            Glide.with(context)
                .load(imageUrl)
                .transform(CenterCrop(), RoundedCorners(30))
                .placeholder(R.drawable.background_pamphlet)
                .into(binding.ivMyRecordPamphlet)
            binding.tvItemMyRecordCreateTime.text = item.createTime
        }

        private fun moveDetailFragment(pamphletId : Long) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener?.moveRecordDetail(pamphletId, itemView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecordViewHolder {
        val binding = ItemMyRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}