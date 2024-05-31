package com.gumibom.travelmaker.data.datasource.login

import com.gumibom.travelmaker.data.api.login.LoginService
import com.gumibom.travelmaker.data.dto.request.LoginRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.LoginResponseDTO
import retrofit2.Response
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val loginService : LoginService
) : LoginRemoteDataSource {
    override suspend fun login(loginRequestDTO: LoginRequestDTO): Response<LoginResponseDTO> {
        return loginService.login(loginRequestDTO)
    }

    override suspend fun findId(phoneNum: String): Response<String> {
        return loginService.findId(phoneNum)
    }
}