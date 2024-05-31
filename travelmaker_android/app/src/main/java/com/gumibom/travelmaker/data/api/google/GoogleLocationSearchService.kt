package com.gumibom.travelmaker.data.api.google

import com.gumibom.travelmaker.data.dto.google.GoogleLocationDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleLocationSearchService {


    @GET("/maps/api/geocode/json")
    suspend fun findGoogleLocationSearch(
        @Query("address") location: String,
        @Query("key") apiKey: String
    ) : Response<GoogleLocationDTO>




}