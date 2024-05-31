package com.gumibom.travelmaker.data.dto.request

import com.gumibom.travelmaker.data.dto.response.Position
import java.util.Date

data class MeetingPostRequestDTO(
    val title : String,
    val username : String,
    val content : String,
    val authDate: String,
    val nativeMin : Int,
    val travelerMin : Int,
    val memberMax : Int,
    val startDate : String,
    val endDate: String,
    val position : Position,
    val deadline : String,
    val categories : List<String>
)
