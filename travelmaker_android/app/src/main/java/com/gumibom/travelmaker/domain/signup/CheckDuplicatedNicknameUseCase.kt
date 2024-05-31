package com.gumibom.travelmaker.domain.signup

import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.SignInResponseDTO
import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import javax.inject.Inject

class CheckDuplicatedNicknameUseCase @Inject constructor(
    private val repository:SignupRepository
){

    suspend fun checkDuplicatedNick(nickname:String): SignInResponseDTO?{
        val response = repository.checkDuplicateNickname(nickname)

        return if (response.isSuccessful){
            response.body()
        }else{
            null
        }
    }
}