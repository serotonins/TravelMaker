package com.gumibom.travelmaker.domain.meeting

import android.util.Log
import com.gumibom.travelmaker.data.dto.request.MarkerPositionRequestDTO
import com.gumibom.travelmaker.data.dto.response.MarkerPositionResponseDTO
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepositoryImpl
import com.gumibom.travelmaker.model.MarkerPosition
import javax.inject.Inject

private const val TAG = "GetMarkerPositionsUseCa_싸피"
class GetMarkerPositionsUseCase @Inject constructor(
    private val meetingRepositoryImpl: MeetingRepository
) {

    suspend fun getMarkerPositions(markerPositionRequestDTO: MarkerPositionRequestDTO) : List<MarkerPosition>{
        val response = meetingRepositoryImpl.getMarkerPositions(markerPositionRequestDTO)
        Log.d(TAG, "getMarkerPositions: $response")
        var markerPositionList = listOf<MarkerPosition>()
        if (response.isSuccessful) {
            // 데이터가 null 일 경우 빈 리스트
            val body = response.body() ?: mutableListOf()

            if (body.isNotEmpty()) {
                markerPositionList = convertLocation(body)
            }
        }
        Log.d(TAG, "getMarkerPositions: $markerPositionList")
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