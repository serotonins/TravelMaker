package com.gumibom.travelmaker.ui.main.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gumibom.travelmaker.data.dto.request.FirebaseResponseRefuseAcceptDTO
import com.gumibom.travelmaker.data.dto.request.ReceivedRequest
import com.gumibom.travelmaker.databinding.ItemFcmNotifyReceivedListBinding
import com.gumibom.travelmaker.model.NotifyReceiveItem
import com.gumibom.travelmaker.ui.main.MainViewModel

class MainFcmNotifyAdapter( private val context : Context,
                            private val viewModel: MainViewModel)
    : ListAdapter<ReceivedRequest, MainFcmNotifyAdapter.RequestViewHolder>(MainNotifyDiffUtil()) {
    // ViewBinding 타입을 변경하세요.
    // 아래 예제에서는 RequestItemLayoutBinding으로 가정합니다.
    inner class RequestViewHolder(private val binding: ItemFcmNotifyReceivedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReceivedRequest) {
            with(binding) {

//                Glide.with(context).load(item.requestorImg).into(imageView)
                Glide.with(context)
                    .load(item.requestorImg)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                    .transform(CenterCrop())
                    .into(imageView)

//               ivRequesterTrust.setImageResource(item.requestorBelief)
                //신뢰도에 따라서 이미지를 매칭시켜야 됨.
                //신뢰도에 따른 이미지 매칭 메소드 구현
                tvRequesterId.text = item.requestorName
                tvGroupId.text = item.meetingPostTitle
                btnNotifyYes.setOnClickListener {
                    // 수락 버튼 클릭 처리
                    viewModel.acceptCrew(FirebaseResponseRefuseAcceptDTO(item.meetingPostId,item.requestId,item.requestorId))
                    removeItem(adapterPosition)
                }
                btnNotifyNo.setOnClickListener {
                    // 거절 버튼 클릭 처리
                    viewModel.refuseCrew(FirebaseResponseRefuseAcceptDTO(item.meetingPostId,item.requestId,item.requestorId))
                }
            }
        }
    }

    //리스트에서 제거.
    override fun submitList(list: List<ReceivedRequest>?) {
        super.submitList(list?.let { ArrayList(it) }) // 리스트의 깊은 복사본을 생성하여 전달
        items = list ?: emptyList() // 어댑터의 리스트를 업데이트
    }
    private var items: List<ReceivedRequest> = emptyList() // 현재 리스트 상태 관리
    private fun removeItem(position: Int) {
        val newList = items.toMutableList() // 현재 리스트를 복사하여 새로운 리스트 생성
        newList.removeAt(position) // 항목 제거
        items = newList // 어댑터의 리스트를 업데이트
        submitList(items) // 변경된 리스트를 submitList()에 전달하여 UI 업데이트
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        // ViewBinding을 사용하여 레이아웃 인플레이트
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFcmNotifyReceivedListBinding.inflate(layoutInflater, parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}