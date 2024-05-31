package com.gumibom.travelmaker.data.repository.google

import com.gumibom.travelmaker.data.dto.google.GoogleLocationDTO
import retrofit2.Response

interface GoogleLocationRepository {
    suspend fun findGoogleLocationSearch(
        location : String,
        apiKey : String) : Response<GoogleLocationDTO>
}