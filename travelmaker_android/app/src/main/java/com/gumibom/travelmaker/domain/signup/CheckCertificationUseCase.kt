package com.gumibom.travelmaker.domain.signup

import com.gumibom.travelmaker.data.dto.request.PhoneCertificationRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import javax.inject.Inject

class CheckCertificationUseCase @Inject constructor(
    private val signupRepositoryImpl: SignupRepository
) {
    suspend fun isCertificationNumber(phoneCertificationRequestDTO: PhoneCertificationRequestDTO) : Boolean{
        val response = signupRepositoryImpl.isCertificationNumber(phoneCertificationRequestDTO)

        var isSuccess = false

        if (response.isSuccessful) {
            isSuccess = response.body()?.isSuccess == true
        }
        return isSuccess
    }

}