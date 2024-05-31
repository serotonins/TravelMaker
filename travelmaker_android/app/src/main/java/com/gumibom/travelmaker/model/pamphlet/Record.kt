package com.gumibom.travelmaker.model.pamphlet

data class Record(
    val recordId: Long,
    val title: String,
    val createTime: String,
    val imgUrl: String,
    val videoUrl: String,
    val videoThumbnailUrl: String,
    val text: String,
    val emoji: String
)