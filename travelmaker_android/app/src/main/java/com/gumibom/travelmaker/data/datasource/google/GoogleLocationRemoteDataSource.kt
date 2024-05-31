package com.gumibom.travelmaker.data.datasource.google

import com.gumibom.travelmaker.data.dto.google.GoogleLocationDTO
import retrofit2.Response

interface GoogleLocationRemoteDataSource {

    suspend fun findGoogleLocationSearch(
        location : String,
        apiKey : String) : Response<GoogleLocationDTO>
}