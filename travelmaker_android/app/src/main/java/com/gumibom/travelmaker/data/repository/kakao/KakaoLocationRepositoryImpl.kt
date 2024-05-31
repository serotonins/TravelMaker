package com.gumibom.travelmaker.data.repository.kakao

import com.gumibom.travelmaker.data.datasource.kakao.KakaoLocationRemoteDataSource
import com.gumibom.travelmaker.data.datasource.kakao.KakaoLocationRemoteDataSourceImpl
import com.gumibom.travelmaker.data.dto.kakao.KakaoLocationDTO
import retrofit2.Response
import javax.inject.Inject

class KakaoLocationRepositoryImpl @Inject constructor(
    private val kakaoLocationRemoteDataSourceImpl: KakaoLocationRemoteDataSource
) : KakaoLocationRepository {
    override suspend fun getKakaoLocation(
        apiKey: String,
        location: String
    ): Response<KakaoLocationDTO> {
        return kakaoLocationRemoteDataSourceImpl.getKakaoLocation(apiKey, location)
    }
}