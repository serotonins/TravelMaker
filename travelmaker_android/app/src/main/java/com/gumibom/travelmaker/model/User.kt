package com.gumibom.travelmaker.model

data class User(
    val userId : Long,
    val username : String,
    val nickname : String,
    val birth : String,
    val profileImgURL : String,
    val trust : Double,
    val town : String,
    val nation : String,
    val categories : List<String>
)

