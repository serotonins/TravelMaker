package com.gumibom.travelmaker.data.api.myPage

import com.gumibom.travelmaker.data.dto.response.UserResponseDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET

interface MyPageService {

    @GET("/mypage")
    suspend fun getMyUserInfo() : Response<UserResponseDTO>

    @DELETE("/user/withdrawal")
    suspend fun deleteMyInfo()





}