package com.gumibom.travelmaker.data.dto.request

data class ReceivedRequest(
    val acceptorName: String,
    val meetingPostId: Int,
    val meetingPostTitle: String,
    val requestId: Int,
    val requestorBelief: Int,
    val requestorId: Int,
    val requestorImg: String,
    val requestorName: String
)