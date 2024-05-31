package com.gumibom.travelmaker.domain.meeting

import android.util.Log
import com.gumibom.travelmaker.data.dto.mygroup.MyMeetingGroupDTO
import com.gumibom.travelmaker.data.dto.mygroup.MyMeetingGroupDTOItem
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepositoryImpl
import javax.inject.Inject

private const val TAG = "GetGroupList"
class GetMyMeetingGroupListUseCase @Inject constructor(
    private val meetingRepositoryImpl: MeetingRepository
) {

    suspend fun getMyGroupList(id:Long):List<MyMeetingGroupDTOItem>{
        val response = meetingRepositoryImpl.getGroupList(id)
        var myGroupList = listOf<MyMeetingGroupDTOItem>()
        Log.d(TAG, "getMyGroupList: ${response.isSuccessful}")
        Log.d(TAG, "getMyGroupList: ${response.errorBody()}")
        if (response.isSuccessful) {

            val body = response.body()?: mutableListOf()
            Log.d(TAG, "getMyGroupList: ${body.toString()}")
            if (body.isNotEmpty()){
                myGroupList = body
            }
        }
        return myGroupList
    }


}