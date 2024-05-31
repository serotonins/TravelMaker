package com.gumibom.travelmaker.data.datasource.kakao

import com.gumibom.travelmaker.data.dto.kakao.KakaoLocationDTO
import retrofit2.Response

interface KakaoLocationRemoteDataSource {
    suspend fun getKakaoLocation(apiKey : String, location : String) : Response<KakaoLocationDTO>
}