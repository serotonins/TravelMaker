package com.gumibom.travelmaker.ui.main.mygroup

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.data.dto.mygroup.MyMeetingGroupDTOItem
import com.gumibom.travelmaker.data.dto.request.ReceivedRequest
import com.gumibom.travelmaker.databinding.ItemFcmNotifyReceivedListBinding
import com.gumibom.travelmaker.databinding.ItemMygroupListBinding
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.ui.main.notification.MainFcmNotifyAdapter

private const val TAG = "MyGroupAdapter"
class MainMyGroupAdapter(
    private val context: MainActivity,
    private val viewModel: MainViewModel
) : ListAdapter<MyMeetingGroupDTOItem, MainMyGroupAdapter.GroupListViewHolder>(MainMyGroupDiffUtil()) {


    inner class GroupListViewHolder(private val binding: ItemMygroupListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: MyMeetingGroupDTOItem) {
            with(binding) {
                Glide.with(context).load(item.mainImgUrl).into(imageViewPhoto)
                tvGroupTitle.text = item.postTitle
                Log.d(TAG, "bind: ${item.numOfTraveler}")
                Log.d(TAG, "bind: ${item.numOfNative}")
                ivMyGroupLocalPersonCnt.text = item.numOfNative.toString()
                ivMyGroupTripPersonCnt.text = item.numOfTraveler.toString()
                ivMyGroupMaxPersonCnt.text = (item.numOfTraveler + item.numOfTraveler).toString()
                tvPlaceAddress.text = item.position.name + item.position.town
                tvStartDay.text = item.startDate.toString()
                btnCancelGroup.setOnClickListener {
                    //모임 취소하기.
                }
                btnStartGroupChatting.setOnClickListener {
                    //그룹 체팅 입장하기.
                    binding.btnStartGroupChatting.text = "채팅 입장"
                    context.navigationToGroupChattingRoom(item.postId)
                }
                btnStartGroup.setOnClickListener {
                    if (item.headId == viewModel.user.value!!.userId.toInt()) {//내가 방장일 때
                        //isFinished 활성화 시키는 api통신 기능 구현
                        //시작을 누를 수 있다.
                        viewModel.putActiveChatting(item.postId)
                        //그룹 체팅방 입장 로직 담기/
                        binding.btnStartGroupChatting.text = "채팅 입장"
                        binding.btnStartGroup.visibility = View.INVISIBLE
                        btnStartGroupChatting.visibility = View.VISIBLE
                    } else if (item.headId != viewModel.user.value!!.userId.toInt()) {//내가 방장이 아닐 때
                        if (item.isFinish) {
                            //그룹체팅 입장.
                            //파이어베이스로 활용할 계획
                            binding.btnStartGroup.visibility = View.INVISIBLE
                            btnStartGroupChatting.visibility = View.VISIBLE
                        }
                    }
                }
            }
            if (item.headId == viewModel.user.value!!.userId.toInt()) {
                if (!item.isFinish) {
                    binding.btnStartGroup.text = "모임 시작"
                } else {
                    binding.btnStartGroup.text = "모임 종료"
                    binding.btnStartGroupChatting.text = "채팅 입장"
                    binding.btnStartGroup.isEnabled = false
                    binding.btnStartGroup.visibility = View.INVISIBLE
                    binding.btnStartGroupChatting.visibility = View.VISIBLE
                }
            } else if (item.headId != viewModel.user.value!!.userId.toInt()) {//내가 방장이 아닐 때 버튼 활성화 기능
                binding.btnHeadImage.visibility = View.GONE
                if (!item.isFinish) {
                    binding.btnStartGroup.text = "아직 모임이 \n 시작 안 됐어요!"
                    binding.btnStartGroup.isEnabled = false
                } else {//종료된 상태면 입장 가능 ~!
                    binding.btnStartGroup.isEnabled = true
                    binding.btnStartGroup.visibility = View.GONE
                    binding.btnStartGroup.visibility = View.INVISIBLE
                    binding.btnStartGroupChatting.visibility = View.VISIBLE
                    binding.btnStartGroupChatting.text = "채팅 입장"
                }
            }

        }
    }
        override fun submitList(list: List<MyMeetingGroupDTOItem>?) {
        super.submitList(list?.let { ArrayList(it) }) // 리스트의 깊은 복사본을 생성하여 전달
        items = list ?: emptyList() // 어댑터의 리스트를 업데이트
    }
    private var items: List<MyMeetingGroupDTOItem> = emptyList() // 현재 리스트 상태 관리
    private fun removeItem(position: Int) {
        val newList = items.toMutableList() // 현재 리스트를 복사하여 새로운 리스트 생성
        newList.removeAt(position) // 항목 제거
        items = newList // 어댑터의 리스트를 업데이트
        submitList(items) // 변경된 리스트를 submitList()에 전달하여 UI 업데이트
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMygroupListBinding.inflate(layoutInflater, parent, false)
        return GroupListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}


//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMyGroupAdapter.GroupListViewHolder {
//        // ViewBinding을 사용하여 레이아웃 인플레이트
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding = ItemFcmNotifyReceivedListBinding.inflate(layoutInflater, parent, false)
//        return RequestViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MainMyGroupAdapter.GroupListViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//    }

//
//
//
//
//
//}