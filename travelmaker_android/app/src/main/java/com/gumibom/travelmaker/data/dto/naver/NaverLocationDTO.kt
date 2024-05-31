package com.gumibom.travelmaker.data.dto.naver

import com.gumibom.travelmaker.data.dto.naver.AddressDTO

data class NaverLocationDTO(
    val lastBuildDate : String?,
    val total : Int?,
    val start : Int?,
    val display : Int?,
    val items : MutableList<AddressDTO>?
)
