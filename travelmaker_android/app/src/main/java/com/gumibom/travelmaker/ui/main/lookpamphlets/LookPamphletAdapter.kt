package com.gumibom.travelmaker.ui.main.lookpamphlets

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.ItemPamphletBinding
import com.gumibom.travelmaker.model.pamphlet.PamphletItem
import com.gumibom.travelmaker.ui.main.myrecord.ItemClickListener

private const val TAG = "LookPamphletAdapter_싸피"
class LookPamphletAdapter(private val context : Context) : ListAdapter<PamphletItem, LookPamphletAdapter.LookPamphletViewHolder>(LookPamphletDiffUtil()) {

    var itemClickListener: ItemClickListener? = null
    inner class LookPamphletViewHolder(private val binding : ItemPamphletBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : PamphletItem) {
            setItem(item)
            setCategoryAdapter(item)

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

        }

        /**
         * 카테고리 리싸이클러뷰 선언
         */
        private fun setCategoryAdapter(item : PamphletItem) {
            val adapter = CategoryAdapter()
            binding.rvCategory.adapter = adapter
            adapter.submitList(item.categories)
        }

        /**
         * Detail Fragment로 넘어가는 함수
         */
        private fun moveDetailFragment(pamphletId : Long) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                Log.d(TAG, "moveDetailFragment: $itemClickListener")
                itemClickListener?.moveRecordDetail(pamphletId, itemView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookPamphletViewHolder {
        val binding = ItemPamphletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LookPamphletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LookPamphletViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}