package com.gumibom.travelmaker.domain.firebase

import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.data.dto.request.FirebaseResponseRefuseAcceptDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.firebase.FirebaseFcmRepository
import com.gumibom.travelmaker.model.BooleanResponse
import javax.inject.Inject

class FirebaseAcceptCrewUseCase @Inject constructor(
    private val firebaseFcmRepository: FirebaseFcmRepository
) {
    suspend fun acceptCrew(fcmRequestGroupDTO: FirebaseResponseRefuseAcceptDTO):BooleanResponse{
        val response = firebaseFcmRepository.acceptCrew(fcmRequestGroupDTO)
        if (response.isSuccessful){
            return convertFcmResult(response.body())
        }
        return convertFcmResult(response.body())
    }

    private fun convertFcmResult(body: IsSuccessResponseDTO?): BooleanResponse {
        if (body != null && body.isSuccess) {//바디가 널값이 아니면서 성공했다면?
            return BooleanResponse(true, body.message)
        } else if (body != null) {
            return BooleanResponse(false, body.message)
        }
        return BooleanResponse(false, "No response from server")
    }
}