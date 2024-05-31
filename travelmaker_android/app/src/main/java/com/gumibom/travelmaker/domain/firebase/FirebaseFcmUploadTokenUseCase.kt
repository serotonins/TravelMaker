package com.gumibom.travelmaker.domain.firebase

import com.gumibom.travelmaker.data.datasource.firebase.FirebaseFcmRemoteDataSource
import com.gumibom.travelmaker.data.datasource.firebase.FirebaseFcmRemoteDataSourceImpl
import com.gumibom.travelmaker.data.dto.request.FcmTokenRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.firebase.FirebaseFcmRepository
import com.gumibom.travelmaker.model.BooleanResponse
import retrofit2.http.Body
import javax.inject.Inject

private const val TAG = "FirebaseFcmUploadTokenU_asdf"
class FirebaseFcmUploadTokenUseCase @Inject constructor(
    private val firebaseFcmRepository: FirebaseFcmRepository
){
    //viewmodel과 통신하기.
    //이우건!!!!!!!!!!!!!
    //같이 화이팅하자!
    suspend fun uploadToken(firebaseFcmTokenRequestDTO: FcmTokenRequestDTO):BooleanResponse{
        val response = firebaseFcmRepository.uploadToken(firebaseFcmTokenRequestDTO)
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