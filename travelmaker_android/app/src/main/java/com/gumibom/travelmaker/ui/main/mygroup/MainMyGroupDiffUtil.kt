package com.gumibom.travelmaker.ui.main.mygroup

import androidx.recyclerview.widget.DiffUtil
import com.gumibom.travelmaker.data.dto.mygroup.MyMeetingGroupDTOItem

class MainMyGroupDiffUtil : DiffUtil.ItemCallback<MyMeetingGroupDTOItem>(){
    override fun areItemsTheSame(
        oldItem: MyMeetingGroupDTOItem,
        newItem: MyMeetingGroupDTOItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MyMeetingGroupDTOItem,
        newItem: MyMeetingGroupDTOItem
    ): Boolean {
        return oldItem.postTitle == newItem.postTitle
    }

}
//class MainMyGroupDiffUtil:DiffUtil.ItemCallback</** dasf */>{
//
//}