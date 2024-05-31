package com.gumibom.travelmaker.data.datasource.naver

import com.gumibom.travelmaker.data.dto.naver.NaverLocationDTO
import retrofit2.Response

interface NaverLocationRemoteDataSource {
    suspend fun findNaverLocationSearch(
        idKey : String,
        secretKey: String,
        location : String,
        display : Int) : Response<NaverLocationDTO>

}