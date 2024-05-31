package com.gumibom.travelmaker.data.dto.response

import com.google.gson.annotations.SerializedName

data class IsSuccessResponseDTO(
    @SerializedName("isSuccess")
    val isSuccess : Boolean,
    @SerializedName("message")
    val message : String
)
