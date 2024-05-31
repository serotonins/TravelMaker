package com.gumibom.travelmaker.data.datasource.naver

import com.gumibom.travelmaker.data.api.naver.NaverLocationSearchService
import com.gumibom.travelmaker.data.dto.naver.NaverLocationDTO
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "NaverLocationRemoteData_싸피"
class NaverLocationRemoteDataSourceImpl @Inject constructor(
    private val naverLocationService : NaverLocationSearchService
) : NaverLocationRemoteDataSource {

    //네이버 위치 검색
    override suspend fun findNaverLocationSearch(
        idKey: String,
        secretKey: String,
        location: String,
        display: Int
    ): Response<NaverLocationDTO> {
        return naverLocationService.findNaverLocationSearch(idKey, secretKey, location, display)
    }
}