package com.gumibom.travelmaker.data.repository.myPage

import com.gumibom.travelmaker.data.dto.response.UserResponseDTO
import retrofit2.Response

interface MyPageRepository {
    suspend fun getMyUserInfo() : Response<UserResponseDTO>
    suspend fun deleteMyInfo()
}