package com.gumibom.travelmaker.data.datasource.meeting_post

import com.gumibom.travelmaker.data.dto.request.MeetingPostRequestDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response


interface MeetingPostRemoteDataSource {

    suspend fun createMeeting(
        imgUrlMain : MultipartBody.Part,
        imgUrlSub : MultipartBody.Part?,
        imgUrlThr : MultipartBody.Part?,
        meetingPostRequestDTO : RequestBody
    ) : Response<String>

}