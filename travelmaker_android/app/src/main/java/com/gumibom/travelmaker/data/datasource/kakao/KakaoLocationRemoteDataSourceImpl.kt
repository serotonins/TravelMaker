package com.gumibom.travelmaker.data.datasource.kakao

import com.gumibom.travelmaker.data.api.kakao.KakaoLocationSearchService
import com.gumibom.travelmaker.data.dto.kakao.KakaoLocationDTO
import retrofit2.Response
import javax.inject.Inject

class KakaoLocationRemoteDataSourceImpl @Inject constructor(
    private val kakaoLocationSearchService: KakaoLocationSearchService
) : KakaoLocationRemoteDataSource {
    override suspend fun getKakaoLocation(
        apiKey: String,
        location: String
    ): Response<KakaoLocationDTO> {
        return kakaoLocationSearchService.getKakaoLocation(apiKey, location)
    }
}