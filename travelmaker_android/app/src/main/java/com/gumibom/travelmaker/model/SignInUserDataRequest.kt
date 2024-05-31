package com.gumibom.travelmaker.model

import com.gumibom.travelmaker.data.dto.request.RequestDto

data class SignInUserDataRequest(
    var image: String,
    val requestDto: RequestUserData
)