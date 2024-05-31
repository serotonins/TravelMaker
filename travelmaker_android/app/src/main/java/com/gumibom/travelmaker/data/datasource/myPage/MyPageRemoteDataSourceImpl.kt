package com.gumibom.travelmaker.data.datasource.myPage

import com.gumibom.travelmaker.data.api.myPage.MyPageService
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.UserResponseDTO
import retrofit2.Response
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val myPageService : MyPageService
) : MyPageRemoteDataSource {
    override suspend fun getMyUserInfo(): Response<UserResponseDTO> {
        return myPageService.getMyUserInfo()
    }

    override suspend fun deleteMyInfo() {
    }

//    override suspend fun myPage(): Response<IsSuccessResponseDTO> {
//        return myPageService.getMyUserInfo()
//
//    }
}