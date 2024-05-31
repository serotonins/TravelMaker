package com.gumibom.travelmaker.ui.main.myrecord.createRecord

import androidx.recyclerview.widget.DiffUtil
import com.gumibom.travelmaker.model.pamphlet.Emoji

class MyRecordEmojiDiffUtil : DiffUtil.ItemCallback<Emoji>() {
    override fun areItemsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem.emojiId == newItem.emojiId
    }

    override fun areContentsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem == newItem
    }
}