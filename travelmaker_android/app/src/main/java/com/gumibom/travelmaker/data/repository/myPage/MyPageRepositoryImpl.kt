package com.gumibom.travelmaker.data.repository.myPage

import com.gumibom.travelmaker.data.datasource.myPage.MyPageRemoteDataSource
import com.gumibom.travelmaker.data.datasource.myPage.MyPageRemoteDataSourceImpl
import com.gumibom.travelmaker.data.dto.response.UserResponseDTO
import retrofit2.Response
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageRemoteDataSourceImpl: MyPageRemoteDataSource
) : MyPageRepository {

    override suspend fun getMyUserInfo(): Response<UserResponseDTO> {
        return myPageRemoteDataSourceImpl.getMyUserInfo()
    }


    override suspend fun deleteMyInfo() {

    }


}