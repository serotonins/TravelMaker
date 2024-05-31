package com.gumibom.travelmaker.domain.meeting

import com.gumibom.travelmaker.data.dto.request.MarkerCategoryPositionRequestDTO
import com.gumibom.travelmaker.data.dto.request.MarkerPositionRequestDTO
import com.gumibom.travelmaker.data.dto.response.MarkerPositionResponseDTO
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepositoryImpl
import com.gumibom.travelmaker.model.MarkerPosition
import javax.inject.Inject

class GetMarkerCategoryPositionsUseCase @Inject constructor(
    private val meetingRepositoryImpl: MeetingRepository
) {
    suspend fun getMarkerCategoryPositions(markerCategoryPositionRequestDTO: MarkerCategoryPositionRequestDTO) : List<MarkerPosition>{
        val response = meetingRepositoryImpl.getMarkerCategoryPositions(markerCategoryPositionRequestDTO)
        var markerPositionList = listOf<MarkerPosition>()
        if (response.isSuccessful) {
            // 데이터가 null 일 경우 빈 리스트
            val body = response.body() ?: mutableListOf()

            if (body.isNotEmpty()) {
                markerPositionList = convertLocation(body)
            }
        }
        return markerPositionList
    }

    private fun convertLocation(body : MutableList<MarkerPositionResponseDTO>) : MutableList<MarkerPosition>{
        val markerList = mutableListOf<MarkerPosition>()

        for (marker in body) {
            val markerPosition = MarkerPosition(
                marker.id ?: 0,
                marker.latitude ?: 0.0,
                marker.longitude ?: 0.0
            )

            markerList.add(markerPosition)
        }
        return markerList
    }
}