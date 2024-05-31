package com.gumibom.travelmaker.domain.pamphlet

import android.util.Log
import com.gumibom.travelmaker.data.dto.request.DeleteRecordRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepositoryImpl
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "DeleteRecordUseCase_싸피"
class DeleteRecordUseCase @Inject constructor(
    private val pamphletRepositoryImpl: PamphletRepository
) {

    suspend fun deleteRecord(deleteRecordRequestDTO: DeleteRecordRequestDTO) : Boolean  {
        val response = pamphletRepositoryImpl.deleteRecord(deleteRecordRequestDTO)
        Log.d(TAG, "deleteRecord: $response")
        var isSuccess = false

        if (response.isSuccessful) {
            val body = response.body()!!
            isSuccess = body.isSuccess
        }
        return isSuccess
    }
}