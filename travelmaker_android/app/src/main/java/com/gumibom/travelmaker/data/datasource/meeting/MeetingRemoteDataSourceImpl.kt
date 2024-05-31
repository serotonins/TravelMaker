package com.gumibom.travelmaker.data.datasource.meeting

import com.gumibom.travelmaker.data.api.meeting.MeetingService
import com.gumibom.travelmaker.data.dto.mygroup.MyMeetingGroupDTOItem
import com.gumibom.travelmaker.data.dto.request.MarkerCategoryPositionRequestDTO
import com.gumibom.travelmaker.data.dto.request.MarkerPositionRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.MarkerPositionResponseDTO
import com.gumibom.travelmaker.data.dto.response.MeetingPostDTO
import retrofit2.Response
import javax.inject.Inject

class MeetingRemoteDataSourceImpl @Inject constructor(
    private val meetingService : MeetingService
) : MeetingRemoteDataSource {

    override suspend fun getMarkerPositions(markerPositionRequestDTO: MarkerPositionRequestDTO): Response<MutableList<MarkerPositionResponseDTO>> {
        return meetingService.getMarkerPositions(markerPositionRequestDTO)
    }

    override suspend fun getMarkerCategoryPositions(markerCategoryPositionRequestDTO: MarkerCategoryPositionRequestDTO): Response<MutableList<MarkerPositionResponseDTO>> {
        return meetingService.getMarkerCategoryPositions(markerCategoryPositionRequestDTO)
    }

    override suspend fun getPostDetail(id: Long): Response<MeetingPostDTO> {
        return meetingService.getPostDetail(id)
    }

    override suspend fun getGroupList(id: Long): Response<MutableList<MyMeetingGroupDTOItem>> {
        return meetingService.getGroupList(id)
    }

    override suspend fun putActiveChat(groupId: Long): Response<IsSuccessResponseDTO> {
        return meetingService.putActiveChatting(groupId)
    }



}