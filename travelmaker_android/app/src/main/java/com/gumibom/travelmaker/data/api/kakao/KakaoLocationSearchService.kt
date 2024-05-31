package com.gumibom.travelmaker.data.api.kakao

import com.gumibom.travelmaker.data.dto.kakao.KakaoLocationDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocationSearchService {
    @GET("/v2/local/search/keyword.json")
    suspend fun getKakaoLocation(
        @Header("Authorization") apiKey : String,
        @Query("query") location : String
    ) : Response<KakaoLocationDTO>
}