package com.gumibom.travelmaker.model

data class RequestUserData(
    var username: String,
    var password: String,
    var nickname: String,
    var email: String?,
    var town: String,
    var gender: String,
    var birth: String,
    var phone: String,
    var nation: String,
    var categories: List<String>
)