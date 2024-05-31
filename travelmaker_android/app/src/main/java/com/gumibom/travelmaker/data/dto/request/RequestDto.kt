package com.gumibom.travelmaker.data.dto.request

data class RequestDto(
    val birth: String,
    val categories: List<String>,
    val email: String,
    val gender: String,
    val nation: String,
    val nickname: String,
    val password: String,
    val phone: String,
    val town: String,
    val username: String
)