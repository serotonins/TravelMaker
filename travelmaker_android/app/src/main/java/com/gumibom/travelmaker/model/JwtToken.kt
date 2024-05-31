package com.gumibom.travelmaker.model

data class JwtToken(
    val accessToken : String,
    val refreshToken : String
)
