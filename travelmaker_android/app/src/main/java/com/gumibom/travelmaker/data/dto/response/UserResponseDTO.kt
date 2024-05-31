package com.gumibom.travelmaker.data.dto.response

data class UserResponseDTO(
    val userId : Long,
    val username : String,
    val email : String?,
    val nickname : String,
    val gender : String,
    val birth : String,
    val phone : String,
    val profileImgURL : String?,
    val trust : Double?,
    val town : String,
    val nation : String,
    val categories : List<String>
)
