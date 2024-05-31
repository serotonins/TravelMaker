package com.gumibom.travelmaker.domain.pamphlet

import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import javax.inject.Inject

class FinishTravelPamphletUseCase @Inject constructor(
   private val pamphletRepositoryImpl: PamphletRepository
) {

    suspend fun finishRecordMyPamphlet(pamphletId : Long) : String?{
        val response = pamphletRepositoryImpl.finishRecordMyPamphlet(pamphletId)

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                return body.message
            }
        }
        return null
    }
}