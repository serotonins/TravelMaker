package com.gumibom.travelmaker.data.dto.google

data class GoogleLocationDTO(
    val results: List<Result>?,
    val status: String?
)