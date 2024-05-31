package com.gumibom.travelmaker.domain.firebase

import android.util.Log
import com.gumibom.travelmaker.data.dto.request.FcmGetNotifyListDTO
import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.firebase.FirebaseFcmRepository
import com.gumibom.travelmaker.model.BooleanResponse
import javax.inject.Inject

private const val TAG = "FirebaseNotifyListUseCa"
class FirebaseNotifyListUseCase @Inject constructor(
    private val firebaseFcmRepository: FirebaseFcmRepository
) {
    suspend fun getNotifyList(userId:Long):FcmGetNotifyListDTO?{
        val response = firebaseFcmRepository.getAllRequestList(userId)
        if (response.isSuccessful){
            Log.d(TAG, "getNotifyList: ${response.body()}")
            return response.body()!!
        }
        Log.d(TAG, "getNotifyList: ${response.body()}")
        return response.body()
    }

//    private fun convertFcmResult(body: FcmGetNotifyListDTO?): BooleanResponse {
//        if (body != null && body.isSuccess) {//바디가 널값이 아니면서 성공했다면?
//            return BooleanResponse(true, body.message)
//        } else if (body != null) {
//            return BooleanResponse(false, body.message)
//        }
//        return BooleanResponse(false, "No response from server")
//    }

}