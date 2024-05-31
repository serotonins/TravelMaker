package com.gumibom.travelmaker.data.datasource.login

import com.gumibom.travelmaker.data.dto.request.LoginRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.LoginResponseDTO
import retrofit2.Response

interface LoginRemoteDataSource {
    suspend fun login(loginRequestDTO: LoginRequestDTO) : Response<LoginResponseDTO>

    suspend fun findId(phoneNum : String) : Response<String>
}