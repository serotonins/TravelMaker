package com.gumibom.travelmaker.data.api.naver

import com.gumibom.travelmaker.data.dto.naver.NaverLocationDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverLocationSearchService {

    // 네이버 장소 검색 api로 장소 response 받는 api
    @GET("/v1/search/local.json")
    suspend fun findNaverLocationSearch(
        @Header("X-Naver-Client-Id") idKey : String,
        @Header("X-Naver-Client-Secret") secretKey : String,
        @Query("query") location : String,
        @Query("display") display : Int
    ) : Response<NaverLocationDTO>
}