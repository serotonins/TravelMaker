package com.gumibom.travelmaker.data.repository.login

import com.gumibom.travelmaker.data.dto.request.LoginRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.LoginResponseDTO
import retrofit2.Response

interface LoginRepository {
    suspend fun login(loginRequestDTO: LoginRequestDTO) : Response<LoginResponseDTO>

    suspend fun findId(phoneNum : String) : Response<String>
}