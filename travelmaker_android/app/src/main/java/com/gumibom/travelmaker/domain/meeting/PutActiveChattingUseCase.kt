package com.gumibom.travelmaker.domain.meeting

import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import javax.inject.Inject

class PutActiveChattingUseCase  @Inject constructor(
    private val meetingRepositoryImpl: MeetingRepository
) {

   suspend fun putGroupChat(groupId:Long):IsSuccessResponseDTO{
        val response = meetingRepositoryImpl.putActiveChat(groupId)
       if (response.isSuccessful){
           val body = response.body()
           return body!!
       }
       return IsSuccessResponseDTO(isSuccess = false, message = "response 응답이 되지 않음.")
   }


}