package com.gumibom.travelmaker.ui.main.myrecord.createRecord

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.databinding.ItemEmojiBinding
import com.gumibom.travelmaker.model.pamphlet.Emoji

private const val TAG = "MyRecordEmojiAdapter_싸피"
class MyRecordEmojiAdapter(private val context: Context, private val makeMyRecordViewModel : MakeMyRecordViewModel) : ListAdapter<Emoji, MyRecordEmojiAdapter.MyRecordEmojiViewHolder>(
    MyRecordEmojiDiffUtil()
) {

    // 클릭한 아이템의 position을 갱신하기 위한 변수
    private var selectItemPosition: Int = RecyclerView.NO_POSITION

    inner class MyRecordEmojiViewHolder(private val binding : ItemEmojiBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Emoji, position : Int) {
            Glide.with(context)
                .load(item.emojiId)
                .into(binding.ivEmoji)

            Glide.with(context)
                .load(item.clickItemId)
                .into(binding.ivItemEmojiCheck)

            binding.ivEmoji.setOnClickListener {
                val previousItem = selectItemPosition
                selectItemPosition = position

                // 이전에 선택된 아이템의 체크 표시를 감춥니다.
                notifyItemChanged(previousItem)

                // 현재 선택된 아이템의 체크 표시를 보여줍니다.
                notifyItemChanged(selectItemPosition)
            }

            // 선택된 아이템인지 확인하고 체크 표시를 업데이트합니다.
            if (position == selectItemPosition) {
                binding.ivItemEmojiCheck.visibility = View.VISIBLE
                makeMyRecordViewModel.emojiText = EMOJI_TEXT[position]!!

            } else {
                binding.ivItemEmojiCheck.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecordEmojiViewHolder {
        val binding = ItemEmojiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRecordEmojiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRecordEmojiViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        val EMOJI_TEXT = mapOf<Int, String>(0 to "HAPPY", 1 to "SMILE", 2 to "SOSO", 3 to "SAD", 4 to "ANGRY")
    }

}