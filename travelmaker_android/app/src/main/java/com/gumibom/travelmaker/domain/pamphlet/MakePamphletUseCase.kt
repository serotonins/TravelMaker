package com.gumibom.travelmaker.domain.pamphlet

import android.util.Log
import com.google.gson.Gson
import com.gumibom.travelmaker.data.dto.request.PamphletRequestDTO
import com.gumibom.travelmaker.data.dto.response.PamphletResponseDTO
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

private const val TAG = "MakePamphletUseCase_싸피"
class MakePamphletUseCase @Inject constructor(
    private val pamphletRepositoryImpl: PamphletRepository
) {
    suspend fun makePamphlet(userId : Long, title : String, categories : List<String>, imageUrl : String) : String {
        // TODO 여기서 데이터 변환
        val image = convertMultiPart(imageUrl)
        val pamphletRequestDTO = convertRequestBody(userId, title, categories)

        val response = pamphletRepositoryImpl.makePamphlet(image, pamphletRequestDTO)
        var responseMessage = ""

        Log.d(TAG, "makePamphlet: $response")
        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                responseMessage = body.message
            }
        }
        return responseMessage
    }

    /**
     * Model -> RequestBody
     */
    private fun convertMultiPart(imageUrl : String) : MultipartBody.Part{
        val file = File(imageUrl)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    /**
     * imageUrl -> MultipartBody.part
     */
    private fun convertRequestBody(userId : Long, title : String, categories : List<String>) : RequestBody {
        val pamphletRequestDTO = PamphletRequestDTO(
            userId,
            title,
            categories
        )

        val gson = Gson()
        val productJson = gson.toJson(pamphletRequestDTO)

        return productJson.toRequestBody("application/json".toMediaTypeOrNull())
    }


}