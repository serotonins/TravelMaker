package com.gumibom.travelmaker.data.api.meeting_post

import com.gumibom.travelmaker.data.dto.request.MeetingPostRequestDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MeetingPostService {
    @Multipart
    @POST("/meeting-post/write")
    suspend fun createMeeting(
        @Part imgUrlMain : MultipartBody.Part,
        @Part imgUrlSub : MultipartBody.Part?,
        @Part imgUrlThr : MultipartBody.Part?,
        @Part("requestDTO") meetingPostRequestDTO : RequestBody,
    ) : Response<String>
}