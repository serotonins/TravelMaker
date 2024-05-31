package com.gumibom.travelmaker.data.repository.signup

import com.gumibom.travelmaker.data.dto.request.PhoneCertificationRequestDTO
import com.gumibom.travelmaker.data.dto.request.PhoneNumberRequestDTO
import com.gumibom.travelmaker.data.dto.request.SignInUserDataRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.SignInResponseDTO
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response

interface SignupRepository {


    suspend fun sendPhoneNumber(phoneNumberRequestDTO : PhoneNumberRequestDTO) : Response<IsSuccessResponseDTO>
    suspend fun checkDuplicatedId(id:String): Response<SignInResponseDTO>

    suspend fun checkDuplicateNickname(nickname:String) :Response<SignInResponseDTO> //닉네임 중복 체크


    //유저 데이터 저장 하는 로직
    suspend fun saveUserData(userInfo: RequestBody, profileImage: MultipartBody.Part):Response<IsSuccessResponseDTO> //회원가입 데이터 저장

    suspend fun isCertificationNumber(phoneCertificationRequestDTO: PhoneCertificationRequestDTO) : Response<IsSuccessResponseDTO>
}