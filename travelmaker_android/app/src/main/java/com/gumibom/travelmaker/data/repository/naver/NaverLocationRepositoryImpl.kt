package com.gumibom.travelmaker.data.repository.naver

import com.gumibom.travelmaker.data.datasource.naver.NaverLocationRemoteDataSource
import com.gumibom.travelmaker.data.dto.naver.NaverLocationDTO
import retrofit2.Response
import javax.inject.Inject

class NaverLocationRepositoryImpl @Inject constructor(
    private val naverLocationRemoteDataSourceImpl: NaverLocationRemoteDataSource
) : NaverLocationRepository {

    // 네이버 위치 검색
    override suspend fun findNaverLocationSearch(
        idKey : String,
        secretKey: String,
        location: String,
        display: Int
    ): Response<NaverLocationDTO> {
        return naverLocationRemoteDataSourceImpl.findNaverLocationSearch(idKey, secretKey, location, display)
    }



}


