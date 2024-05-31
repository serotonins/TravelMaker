package com.gumibom.travelmaker.domain.meeting_post

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.gumibom.travelmaker.data.dto.request.MeetingPostRequestDTO
import com.gumibom.travelmaker.data.dto.response.Position
import com.gumibom.travelmaker.data.repository.meeting_post.MeetingPostRepository
import com.gumibom.travelmaker.model.SendMeetingPost
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.TimeZone
import javax.inject.Inject

private const val TAG = "PostMeetingUseCase_싸피"
class PostMeetingUseCase @Inject constructor(
    private val meetingPostRepositoryImpl: MeetingPostRepository
) {

    suspend fun createMeeting(sendMeeting : SendMeetingPost, imageUrlList : MutableList<String>) : String {
        val requestBody = transformModelToDto(sendMeeting)

        var imgUrlMain : MultipartBody.Part? = null
        var imgUrlSub : MultipartBody.Part? = null
        var imgUrlThr : MultipartBody.Part? = null

        for (index in 0 until imageUrlList.size) {
            when (index) {
                0 -> {
                    imgUrlMain = convertMultipart(imageUrlList[index], index)
                }
                1 -> {
                    imgUrlSub = convertMultipart(imageUrlList[index], index)
                }
                else -> {
                    imgUrlThr = convertMultipart(imageUrlList[index], index)
                }
            }
        }

        val response = meetingPostRepositoryImpl.createMeeting(
            imgUrlMain!!,
            imgUrlSub,
            imgUrlThr,
            requestBody
        )
        Log.d(TAG, "response: $response")
        if (response.isSuccessful) {
            val body = response.body()
            Log.d(TAG, "createMeeting: $body")
            if (body != null) {
                return body
            }
        }
        return ""
    }

    /**
     *   Model to RequestDTO 변환
     */
    private fun transformModelToDto(sendMeeting: SendMeetingPost): RequestBody {
        val startDate = convertToDate(sendMeeting.startDate) ?: ""
        val endDate = convertToDate(sendMeeting.endDate) ?: ""
        val deadlineDate =
            convertToDateWithTime(sendMeeting.deadlineDate + " " + sendMeeting.deadlineTime) ?: ""
        val position = Position(
            sendMeeting.address.latitude,
            sendMeeting.address.longitude,
            sendMeeting.address.title ?: "",
            sendMeeting.address.address
        )

        val meetingPostRequestDTO = MeetingPostRequestDTO(
            sendMeeting.title,
            sendMeeting.username,
            sendMeeting.content,
            "",
            sendMeeting.minNative,
            sendMeeting.minTraveler,
            sendMeeting.maxMember,
            startDate,
            endDate,
            position,
            deadlineDate,
            sendMeeting.categoryList
        )
        // requestBody 만드는 과정

        return createRequestBody(meetingPostRequestDTO)
    }

    /**
     * RequestDTO -> RequestBody로 변환
     */
    private fun createRequestBody(meetingPostRequestDTO : MeetingPostRequestDTO) : RequestBody {
        val gson = Gson()
        val productJson = gson.toJson(meetingPostRequestDTO)

        return productJson.toRequestBody("application/json".toMediaTypeOrNull())
    }

    /**
     * yyyy-MM-dd => UTC Time zone으로 변환
     */
    @SuppressLint("SimpleDateFormat")
    fun convertToDate(inputDateString: String): String? {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd")
        inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

        try  {
            val date = inputDateFormat.parse(inputDateString)

            if (date != null) {
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                outputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

                return outputDateFormat.format(date)
            }
        } catch (e : Exception) {
            Log.d(TAG, "convertToDate: ${e.message}")
        }
        return null
    }

    /**
     * yyyy-MM-dd HH:mm => UTC Time zone 변환
     */
    @SuppressLint("SimpleDateFormat")
    fun convertToDateWithTime(inputDateString: String): String? {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

        try  {
            val date = inputDateFormat.parse(inputDateString)

            if (date != null) {
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                outputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

                return outputDateFormat.format(date)
            }
        } catch (e : Exception) {
            Log.d(TAG, "convertToDate: ${e.message}")
        }
        return null
    }

    private fun convertMultipart(filePath : String, index : Int) : MultipartBody.Part? {
        // file을 MultipartBody.part로 변환
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        return when(index) {
            0 -> MultipartBody.Part.createFormData("mainImage", file.name, requestFile)
            1 -> MultipartBody.Part.createFormData("subImage", file.name, requestFile)
            2 -> MultipartBody.Part.createFormData("thirdImage", file.name, requestFile)
            else -> null
        }
    }

}