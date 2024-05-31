package com.gumibom.travelmaker.domain.signup

import android.util.Log
import com.gumibom.travelmaker.data.dto.request.PhoneNumberRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import javax.inject.Inject

private const val TAG = "SendPhoneNumberUseCase_싸피"
class SendPhoneNumberUseCase @Inject constructor(
    private val signupRepositoryImpl : SignupRepository
) {
    suspend fun sendPhoneNumber(phoneNumberRequestDTO : PhoneNumberRequestDTO) : Boolean {
        val response = signupRepositoryImpl.sendPhoneNumber(phoneNumberRequestDTO)
        var isSuccess = false

        if (response.isSuccessful) {
            isSuccess = response.body()?.isSuccess == true
        }
        return isSuccess
    }
}