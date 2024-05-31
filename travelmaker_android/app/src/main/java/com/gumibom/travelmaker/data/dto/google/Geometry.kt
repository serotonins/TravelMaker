package com.gumibom.travelmaker.data.dto.google

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)