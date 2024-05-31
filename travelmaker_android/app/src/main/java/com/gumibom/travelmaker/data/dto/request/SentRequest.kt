package com.gumibom.travelmaker.data.dto.request

data class SentRequest(
    val acceptorName: String,
    val meetingPostDeadline: String,
    val meetingPostMainImg: String,
    val meetingPostTitle: String,
    val requestId: Int,
    val requestorName: String
)