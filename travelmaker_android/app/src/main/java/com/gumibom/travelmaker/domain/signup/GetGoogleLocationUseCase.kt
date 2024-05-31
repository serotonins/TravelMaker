package com.gumibom.travelmaker.domain.signup

import com.gumibom.travelmaker.data.dto.google.GoogleLocationDTO
import com.gumibom.travelmaker.data.repository.google.GoogleLocationRepository
import com.gumibom.travelmaker.model.Address
import javax.inject.Inject

class GetGoogleLocationUseCase @Inject constructor(
    private val googleLocationRepositoryImpl: GoogleLocationRepository
) {
    suspend fun findGoogleLocation(location : String, apiKey : String) : MutableList<Address> {
        // 구글 DTO를 GoogleAddress로 바꾸기

        val response = googleLocationRepositoryImpl.findGoogleLocationSearch(location, apiKey)
        var googleAddressList = mutableListOf<Address>()

        // 응답이 성공이면 DTO -> model로 변환
        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                val googleAddress = convertAddressModel(body)

                if (googleAddress != null) {
                    googleAddressList.add(googleAddress)
                }
            }

        }

        return googleAddressList
    }

    private fun convertAddressModel(body : GoogleLocationDTO?) : Address? {
        // 데이터가 null인 경우 ""로 초기화

        var googleAddress : Address? = null

        if (body?.results!!.isNotEmpty()) {
            val formattedAddress = body.results.get(0).formatted_address
            val country = formattedAddress.split(" ").lastOrNull()

            val latitude = body.results.get(0).geometry.location.lat
            val longitude = body.results.get(0).geometry.location.lng

            googleAddress = Address(
                country,
                formattedAddress,
                latitude,
                longitude
            )
        }

        return googleAddress
    }

}

