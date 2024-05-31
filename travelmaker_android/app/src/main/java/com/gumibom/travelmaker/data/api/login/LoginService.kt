package com.gumibom.travelmaker.data.api.login

import com.gumibom.travelmaker.data.dto.request.LoginRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.LoginResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginService {
    @POST("/login")
    suspend fun login(@Body loginRequestDTO: LoginRequestDTO) : Response<LoginResponseDTO>

    @GET("/user/find-login-id/{phoneNum}")
    suspend fun findId(@Path("phoneNum") phoneNum : String) : Response<String>

    @POST("/user/change-password")
    suspend fun findPassword(@Body newPassword : String) : Response<String>
}