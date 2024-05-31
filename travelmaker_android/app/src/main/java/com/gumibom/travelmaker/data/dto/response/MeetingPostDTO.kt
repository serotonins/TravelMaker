package com.gumibom.travelmaker.data.dto.response

data class MeetingPostDTO(
    val categories: List<String> = emptyList(),
    val headNickname:String = "",
    val dday: Int = 0,
    val headId: Int = 0,
    val mainImgUrl: String = "",
    val numOfNative: Int = 0,
    val numOfTraveler: Int = 0,
    val position: Position = Position(0.0, 0.0,"name","town"), // Position 클래스의 기본값 예시, 실제 구현은 달라질 수 있음
    val postContent: String = "",
    val postTitle: String = "",
    val profileImgUrl: String = "",
    val startDate: String = "",
    val subImgUrl: String = "",
    val thirdImgUrl: String = ""
)