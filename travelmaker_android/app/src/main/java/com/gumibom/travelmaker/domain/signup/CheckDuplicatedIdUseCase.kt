package com.gumibom.travelmaker.domain.signup

import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.SignInResponseDTO
import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import javax.inject.Inject

class CheckDuplicatedIdUseCase @Inject constructor(
    private val repository: SignupRepository
) {
    suspend fun checkDuplicatedId(id:String): SignInResponseDTO?{
        val response = repository.checkDuplicatedId(id)
        return if (response.isSuccessful) {
            val body = response.body()
            body
        } else {
            null
        }
    }
}