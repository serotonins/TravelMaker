package com.gumibom.travelmaker.data.datasource.signup


import com.gumibom.travelmaker.data.dto.request.PhoneCertificationRequestDTO
import com.gumibom.travelmaker.data.dto.request.PhoneNumberRequestDTO
import com.gumibom.travelmaker.data.dto.request.SignInUserDataRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.SignInResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Response

interface SignupRemoteDataSource {

    suspend fun sendPhoneNumber(phoneNumberRequestDTO : PhoneNumberRequestDTO) : Response<IsSuccessResponseDTO>


    suspend fun checkDuplicatedId(id:String): Response<SignInResponseDTO>

    suspend fun checkDuplicateNickname(nickname:String):Response<SignInResponseDTO>
    suspend fun saveUserData(userInfo: RequestBody, imageProfile: MultipartBody.Part) :Response<IsSuccessResponseDTO> //회원가입 데이터 저장

    suspend fun isCertificationNumber(phoneCertificationRequestDTO : PhoneCertificationRequestDTO) : Response<IsSuccessResponseDTO>

}