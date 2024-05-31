package com.gumibom.travelmaker.data.dto.mygroup

data class MyMeetingGroupDTOItem(
    val postId : Long,
    val categories: List<String>,
    val dday: Int,
    val headId: Int,
    val headNickname: String,
    val headTrust: Double,
    val isFinish: Boolean,
    val isMeetingFinish: Boolean,
    val mainImgUrl: String,
    val numOfNative: Int,
    val numOfTraveler: Int,
    val position: Position,
    val postContent: String,
    val postTitle: String,
    val profileImgUrl: String,
    val startDate: String,
    val subImgUrl: String,
    val thirdImgUrl: String
)