package com.gumibom.travelmaker.data.dto.request

data class PamphletRequestDTO(
    val userId : Long,
    val title : String,
    val categories : List<String>
)
