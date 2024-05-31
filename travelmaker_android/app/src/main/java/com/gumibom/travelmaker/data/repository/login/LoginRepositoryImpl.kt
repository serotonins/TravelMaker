package com.gumibom.travelmaker.data.repository.login

import com.gumibom.travelmaker.data.datasource.login.LoginRemoteDataSource
import com.gumibom.travelmaker.data.datasource.login.LoginRemoteDataSourceImpl
import com.gumibom.travelmaker.data.dto.request.LoginRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.LoginResponseDTO
import retrofit2.Response
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSourceImpl: LoginRemoteDataSource
) : LoginRepository {
    override suspend fun login(loginRequestDTO: LoginRequestDTO): Response<LoginResponseDTO> {
        return loginRemoteDataSourceImpl.login(loginRequestDTO)
    }

    override suspend fun findId(phoneNum: String): Response<String> {
        return loginRemoteDataSourceImpl.findId(phoneNum)
    }
}