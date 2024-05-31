package com.gumibom.travelmaker.data.repository.signup

import com.gumibom.travelmaker.data.datasource.signup.SignupRemoteDataSource
import com.gumibom.travelmaker.data.datasource.signup.SignupRemoteDataSourceImpl
import com.gumibom.travelmaker.data.dto.request.PhoneCertificationRequestDTO
import com.gumibom.travelmaker.data.dto.request.PhoneNumberRequestDTO
import com.gumibom.travelmaker.data.dto.request.SignInUserDataRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.SignInResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Response
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(
    private val signupRemoteDataSourceImpl: SignupRemoteDataSource
) : SignupRepository {

    override suspend fun sendPhoneNumber(phoneNumberRequestDTO: PhoneNumberRequestDTO): Response<IsSuccessResponseDTO> {
        return signupRemoteDataSourceImpl.sendPhoneNumber(phoneNumberRequestDTO)
    }
    override suspend fun checkDuplicatedId(id: String): Response<SignInResponseDTO> {
        return signupRemoteDataSourceImpl.checkDuplicatedId(id)
    }


    override suspend fun checkDuplicateNickname(nickname: String): Response<SignInResponseDTO> {
        return signupRemoteDataSourceImpl.checkDuplicateNickname(nickname)
    }

    override suspend fun saveUserData(
        userInfo: RequestBody,
        profileImage: MultipartBody.Part
    ): Response<IsSuccessResponseDTO> {
        return signupRemoteDataSourceImpl.saveUserData(userInfo, profileImage)
    }

    override suspend fun isCertificationNumber(phoneCertificationRequestDTO: PhoneCertificationRequestDTO): Response<IsSuccessResponseDTO> {
        return signupRemoteDataSourceImpl.isCertificationNumber(phoneCertificationRequestDTO)
    }


}