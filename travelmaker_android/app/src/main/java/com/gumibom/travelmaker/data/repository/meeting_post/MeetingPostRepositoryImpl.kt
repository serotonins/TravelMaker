package com.gumibom.travelmaker.data.repository.meeting_post

import com.gumibom.travelmaker.data.datasource.meeting_post.MeetingPostRemoteDataSource
import com.gumibom.travelmaker.data.datasource.meeting_post.MeetingPostRemoteDataSourceImpl
import com.gumibom.travelmaker.data.dto.request.MeetingPostRequestDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class MeetingPostRepositoryImpl @Inject constructor(
    private val meetingPostRemoteDataSourceImpl: MeetingPostRemoteDataSource
) : MeetingPostRepository {
    // 모임 생성 api

    override suspend fun createMeeting(
        imgUrlMain: MultipartBody.Part,
        imgUrlSub: MultipartBody.Part?,
        imgUrlThr: MultipartBody.Part?,
        meetingPostRequestDTO: RequestBody,
    ): Response<String> {
        return meetingPostRemoteDataSourceImpl.createMeeting(
            imgUrlMain,
            imgUrlSub,
            imgUrlThr,
            meetingPostRequestDTO
        )
    }
}