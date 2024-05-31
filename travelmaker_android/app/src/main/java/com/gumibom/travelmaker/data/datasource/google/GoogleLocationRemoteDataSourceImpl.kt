package com.gumibom.travelmaker.data.datasource.google

import com.gumibom.travelmaker.data.api.google.GoogleLocationSearchService
import com.gumibom.travelmaker.data.dto.google.GoogleLocationDTO
import retrofit2.Response
import javax.inject.Inject

class GoogleLocationRemoteDataSourceImpl @Inject constructor(
    private val googleLocationSearchService: GoogleLocationSearchService
) : GoogleLocationRemoteDataSource {

    override suspend fun findGoogleLocationSearch(location: String, apiKey: String): Response<GoogleLocationDTO> {
        return googleLocationSearchService.findGoogleLocationSearch(location, apiKey)
    }
}