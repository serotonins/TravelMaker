package com.gumibom.travelmaker.model.pamphlet

data class PamphletItem(
    val pamphletId: Long,
    val nickname: String,
    val title: String,
    val love: Int,
    val createTime: String,
    val isFinish: Boolean,
    val repreImgUrl : String,
    val categories: List<String>
)