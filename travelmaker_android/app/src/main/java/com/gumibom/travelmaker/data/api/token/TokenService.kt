package com.gumibom.travelmaker.data.api.token

import com.gumibom.travelmaker.data.dto.response.LoginResponseDTO
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenService {

    @POST("/refresh-token")
    suspend fun getNewJwtToken(
        @Header("X-Refresh-Token") refreshToken : String
    ) : Response<LoginResponseDTO>
}