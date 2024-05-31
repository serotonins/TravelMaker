package com.gumibom.travelmaker.data.dto.request

data class MarkerCategoryPositionRequestDTO(
    val latitude : Double,
    val longitude : Double,
    val radius : Double,
    val categories : List<String>
)
