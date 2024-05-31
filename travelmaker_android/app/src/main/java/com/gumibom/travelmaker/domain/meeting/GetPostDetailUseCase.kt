package com.gumibom.travelmaker.domain.meeting

import android.util.Log
import com.gumibom.travelmaker.data.dto.response.MeetingPostDTO
import com.gumibom.travelmaker.data.dto.response.Position
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import com.gumibom.travelmaker.model.PostDetail
import javax.inject.Inject

private const val TAG = "GetPostDetailUseCase_싸피"
class GetPostDetailUseCase @Inject constructor(
    private val meetingRepositoryImpl: MeetingRepository
){
    // 바텀 네비게이션에 있는 정보들을 뿌려주는 역할
    // DTO들로 받은 데이터들을 Model로 변환하고 return시킨다.
    // ViewModel에서 사용ㄷ회어허ㅏㄴ다 ㄴ알허 라ㅣ호
    suspend fun getPostDetail(postId:Long):PostDetail {
        val response = meetingRepositoryImpl.getPostDetail(postId)
        Log.d(TAG, "response: $response")
        if (response.isSuccessful) {
            val body = response.body()
            Log.d(TAG, "body: $body")
            if (body != null) {
                return convertAddressModel(body)
            }
        }
        return convertAddressModel(MeetingPostDTO())
    }
    private fun convertAddressModel(body : MeetingPostDTO) : PostDetail{
        return PostDetail(
            categories = body.categories!!,
            dday = body.dday?:0,
            headNickname =body.headNickname,
            headId = body.headId?:0,
            mainImgUrl = body.mainImgUrl?:"", // String 필드는 null이 아니라고 가정
            numOfNative = body.numOfNative ?: 0, // numOfNative가 null이면 0을 사용
            numOfTraveler = body.numOfTraveler ?: 0, // numOfTraveler가 null이면 0을 사용
            position = body.position ?: Position(0.0, 0.0,"",""), // position이 null이면 기본 Position 객체를 사용
            postContent=body.postContent?:"",
            postTitle = body.postTitle?:"",
            profileImgUrl = body.profileImgUrl ?: "mainIMG",
            startDate = body.startDate ?: "",
            subImgUrl = body.subImgUrl ?: "", // subImgUrl이 null이면 기본 값을 사용
            thirdImgUrl = body.thirdImgUrl ?: "기본_세번째_이미지_URL"
        )
    }
}