package com.gumibom.travelmaker.data.datasource.myPage

import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.UserResponseDTO
import retrofit2.Response

interface MyPageRemoteDataSource {
    suspend fun getMyUserInfo() : Response<UserResponseDTO>
    suspend fun deleteMyInfo()


//
//    suspend fun myPage():Response<IsSuccessResponseDTO>


}