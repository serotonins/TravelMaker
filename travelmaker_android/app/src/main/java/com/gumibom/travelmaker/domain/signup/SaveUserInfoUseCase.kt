package com.gumibom.travelmaker.domain.signup

import android.util.Log
import com.google.gson.Gson
import com.gumibom.travelmaker.data.dto.request.RecordRequestDTO
import com.gumibom.travelmaker.data.dto.request.SignInUserDataRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import com.gumibom.travelmaker.model.RequestUserData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

private const val TAG = "SaveUserInfoUseCase_싸피"
class SaveUserInfoUseCase @Inject constructor(
    private val repository: SignupRepository //레포에 있는 값을 받는다
) {
    //코루틴 통신
    suspend fun saveUserInfo(requestUserData: RequestUserData, profileImage: String): IsSuccessResponseDTO?{
        val requestBody = createRequestBody(requestUserData)
        val profileImage2 = convertImageMultiPart(profileImage)

        val response = repository.saveUserData(requestBody, profileImage2)


        Log.d(TAG, "requestUserData: $requestUserData")
        Log.d(TAG, "saveUserInfo: $response")

        return if (response.isSuccessful){
            response.body()
        }else{
            null
        }
    }
    private fun createRequestBody(requestUserData: RequestUserData): RequestBody {
        val gson = Gson()
        val productJson = gson.toJson(requestUserData)
        return productJson.toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun convertImageMultiPart(image : String): MultipartBody.Part {//이미지 저장하는 로직
        val file = File(image)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

}