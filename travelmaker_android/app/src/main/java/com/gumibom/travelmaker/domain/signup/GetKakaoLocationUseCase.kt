package com.gumibom.travelmaker.domain.signup

import android.util.Log
import com.gumibom.travelmaker.data.dto.kakao.Document
import com.gumibom.travelmaker.data.dto.kakao.KakaoLocationDTO
import com.gumibom.travelmaker.data.repository.kakao.KakaoLocationRepository
import com.gumibom.travelmaker.data.repository.kakao.KakaoLocationRepositoryImpl
import com.gumibom.travelmaker.model.Address
import com.gumibom.travelmaker.model.KakaoAddress
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "GetKakaoLocationUseCase_싸피"

class GetKakaoLocationUseCase @Inject constructor(
    private val kakaoLocationRepositoryImpl: KakaoLocationRepository
) {
    suspend fun getKakaoLocation(apiKey: String, location: String) : MutableList<Address> {
        val response = kakaoLocationRepositoryImpl.getKakaoLocation(apiKey, location)

        var documents = listOf<Document>()
        var result = mutableListOf<Address>()

        if (response.isSuccessful && response.body() != null) {
            documents = response.body()?.documents ?: mutableListOf()
        }

        if (documents.isNotEmpty()) {
            for (document in documents) {
                val title = document.place_name
                val address = document.address_name
                val latitude = document.y.toDouble()
                val longitude = document.x.toDouble()

                result.add(
                    Address(
                        title,
                        address,
                        latitude,
                        longitude
                    )
                )
            }
        }
        return result
    }
}