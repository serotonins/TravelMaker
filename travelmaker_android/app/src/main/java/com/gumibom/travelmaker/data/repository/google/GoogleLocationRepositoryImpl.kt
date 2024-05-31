package com.gumibom.travelmaker.data.repository.google

import com.gumibom.travelmaker.data.datasource.google.GoogleLocationRemoteDataSource
import com.gumibom.travelmaker.data.dto.google.GoogleLocationDTO
import retrofit2.Response
import javax.inject.Inject

class GoogleLocationRepositoryImpl @Inject constructor(
    private val googleLocationRemoteDataSourceImpl: GoogleLocationRemoteDataSource
): GoogleLocationRepository {
    override suspend fun findGoogleLocationSearch(
        location: String,
        apiKey: String
    ): Response<GoogleLocationDTO> {
        return googleLocationRemoteDataSourceImpl.findGoogleLocationSearch(location, apiKey)
    }
}