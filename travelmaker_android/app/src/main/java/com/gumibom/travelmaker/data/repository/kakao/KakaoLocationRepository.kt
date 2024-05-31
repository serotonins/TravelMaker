package com.gumibom.travelmaker.data.repository.kakao

import com.gumibom.travelmaker.data.dto.kakao.KakaoLocationDTO
import retrofit2.Response

interface KakaoLocationRepository {
    suspend fun getKakaoLocation(apiKey : String, location : String) : Response<KakaoLocationDTO>
}