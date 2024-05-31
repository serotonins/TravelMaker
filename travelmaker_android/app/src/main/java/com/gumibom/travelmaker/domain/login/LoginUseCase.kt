package com.gumibom.travelmaker.domain.login

import android.util.Log
import com.gumibom.travelmaker.data.dto.request.LoginRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.LoginResponseDTO
import com.gumibom.travelmaker.data.repository.login.LoginRepository
import com.gumibom.travelmaker.data.repository.login.LoginRepositoryImpl
import com.gumibom.travelmaker.model.BooleanResponse
import com.gumibom.travelmaker.model.JwtToken
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "LoginUseCase_싸피"
class LoginUseCase @Inject constructor(
    private val loginRepositoryImpl: LoginRepository
){
//    NaverLocationRemoteDataSource
    suspend fun login(loginRequestDTO: LoginRequestDTO) : JwtToken {
        val response = loginRepositoryImpl.login(loginRequestDTO)
        var jwtToken = JwtToken("", "")

        Log.d(TAG, "login: $response")
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()

            jwtToken = convertDtoToModel(body)
        }

        return jwtToken
    }

    private fun convertDtoToModel(loginRequestDTO: LoginResponseDTO?) : JwtToken{
        val accessToken = loginRequestDTO?.accessToken ?: ""
        val refreshToken = loginRequestDTO?.refreshToken ?: ""

        return JwtToken(accessToken, refreshToken)
    }

}