package com.gumibom.travelmaker.model

data class SendMeetingPost(
    val title : String,
    val username : String,
    val content : String,
    val authDate : String,
    val startDate : String,
    val endDate : String,
    val address : Address,
    val maxMember : Int,
    val minNative : Int,
    val minTraveler : Int,
    val deadlineDate : String,
    val deadlineTime : String,
    val categoryList : MutableList<String>,
)
