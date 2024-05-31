package com.gumibom.travelmaker.domain.pamphlet

import android.util.Log
import com.google.gson.Gson
import com.gumibom.travelmaker.data.dto.request.RecordRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepositoryImpl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

private const val TAG = "MakeRecordUseCase_싸피"
class MakeRecordUseCase @Inject constructor(
    private val pamphletRepositoryImpl: PamphletRepository
) {

    suspend fun makeRecord(image : String, video : String, recordRequestDTO : RecordRequestDTO) : IsSuccessResponseDTO?{
        val requestBody = createRequestBody(recordRequestDTO)
        var multiImage : MultipartBody.Part? = null
        var multiVideo : MultipartBody.Part? = null

        if (image.isNotEmpty()) {
           multiImage = convertImageMultiPart(image)
        }

        if (video.isNotEmpty()) {
            multiVideo = convertVideoMultiPart(video)
        }

        Log.d(TAG, "requestBody: $requestBody")
        Log.d(TAG, "multiImage: $multiImage")
        Log.d(TAG, "multiVideo: $multiVideo")

        val response = pamphletRepositoryImpl.makeRecord(multiImage, multiVideo, requestBody)
        Log.d(TAG, "makeRecord: $response")

        if (response.isSuccessful) {
            val body = response.body()!!

            return body
        }
        return null
    }

    /**
     * RequestDTO -> RequestBody로 변환
     */
    private fun createRequestBody(recordRequestDTO: RecordRequestDTO): RequestBody {
        val gson = Gson()
        val productJson = gson.toJson(recordRequestDTO)

        return productJson.toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun convertImageMultiPart(image : String): MultipartBody.Part {
        val file = File(image)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun convertVideoMultiPart(video : String): MultipartBody.Part {
        val file = File(video)
        val requestFile = file.asRequestBody("video/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("video", file.name, requestFile)
    }
}