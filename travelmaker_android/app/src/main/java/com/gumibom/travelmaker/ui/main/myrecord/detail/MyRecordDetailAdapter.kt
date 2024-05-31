package com.gumibom.travelmaker.ui.main.myrecord.detail


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.ItemMyRecordBinding
import com.gumibom.travelmaker.databinding.ItemMyRecordDetailBinding
import com.gumibom.travelmaker.model.pamphlet.Record
import com.gumibom.travelmaker.ui.main.myrecord.createRecord.MyRecordEmojiAdapter

private const val TAG = "MyRecordDetailAdapter_싸피"
class MyRecordDetailAdapter(private val context : Context, private val viewModel : MyRecordDetailViewModel) : ListAdapter<Record, MyRecordDetailAdapter.MyRecordDetailViewHolder>(MyRecordDetailDiffUtil()) {

    // 클릭한 아이템의 position을 갱신하기 위한 변수
    private var selectItemPosition: Int = RecyclerView.NO_POSITION

    inner class MyRecordDetailViewHolder(private val binding : ItemMyRecordDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Record, position : Int) {
            Log.d(TAG, "myRecord: $item")
            if (item.imgUrl.isNotEmpty() && item.videoUrl.isEmpty()) {
                Glide.with(context)
                    .load(item.imgUrl)
                    .into(binding.ivItemMyRecordDetail)

            } else if (item.imgUrl.isNotEmpty() && item.videoUrl.isNotEmpty()) {
                Glide.with(context)
                    .load(item.imgUrl)
                    .into(binding.ivItemMyRecordDetail)
            }

            binding.ivItemMyRecordDetail.setOnClickListener {
                val previousItem = selectItemPosition
                selectItemPosition = position

                notifyItemChanged(previousItem)
                notifyItemChanged(selectItemPosition)
            }
            // 아이템을 선택하면 background 입히기
            if (position == selectItemPosition) {
                binding.ivItemMyRecordDetail.setBackgroundResource(R.drawable.background_record_outline)
                viewModel.setRecord(item)
            } else {
                // 이전 position의 아이템 background는 해제
                binding.ivItemMyRecordDetail.background = null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecordDetailViewHolder {
        val binding = ItemMyRecordDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRecordDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRecordDetailViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}