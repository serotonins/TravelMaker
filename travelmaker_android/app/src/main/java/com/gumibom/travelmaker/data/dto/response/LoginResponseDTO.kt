package com.gumibom.travelmaker.data.dto.response

data class LoginResponseDTO (
    val grantType: String,
    val accessToken : String,
    val refreshToken : String
)

