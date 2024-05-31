package com.gumibom.travelmaker.ui.main.findmate.meeting_post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.data.dto.response.Position
import com.gumibom.travelmaker.domain.meeting_post.PostMeetingUseCase
import com.gumibom.travelmaker.model.Address
import com.gumibom.travelmaker.model.SendMeetingPost
import com.gumibom.travelmaker.ui.common.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MeetingPostViewModel_싸피"
@HiltViewModel
class MeetingPostViewModel @Inject constructor(
    private val postMeetingUseCase: PostMeetingUseCase
) : ViewModel(), CommonViewModel {

    /**
     * 모임 생성 데이터 모음
     */
    var title = ""
    var username = ""
    var content = ""
    var authDate = ""
    var startDate = ""
    var endDate = ""
    // position은 viewModel address로 대체하자
    var maxMember = 0
    var minNative = 0
    var minTraveler = 0
    var deadlineDate = ""
    var deadlineTime = ""
    var categoryList = mutableListOf<String>()
    private val _imageUrlList = mutableListOf<String>()


    private val _selectMeetingAddress = MutableLiveData<Address>()
    val selectMeetingAddress : LiveData<Address> = _selectMeetingAddress

    private val _urlLiveData = MutableLiveData<MutableList<String>>()
    val urlLiveData : LiveData<MutableList<String>> = _urlLiveData

    private val _isSuccess = MutableLiveData<String>()
    val isSuccess : LiveData<String> = _isSuccess

    fun meetingSelectAddress(address : Address) {
        _selectMeetingAddress.value = address
    }

    fun addImageUrl(ImageUrl : String) {
        Log.d(TAG, "addImageUrl: $ImageUrl")
        _imageUrlList.add(ImageUrl)

        _urlLiveData.value = _imageUrlList
    }

    fun createMeeting() {
        val sendMeetingPost = SendMeetingPost(
            title,
            username,
            content,
            authDate,
            startDate,
            endDate,
            _selectMeetingAddress.value!!,
            maxMember,
            minNative,
            minTraveler,
            deadlineDate,
            deadlineTime,
            categoryList
        )
        Log.d(TAG, "createMeeting: $sendMeetingPost")
        viewModelScope.launch {
            _isSuccess.value = postMeetingUseCase.createMeeting(sendMeetingPost, _imageUrlList)
        }
    }



    override fun setAddress(address: String) {

    }
}