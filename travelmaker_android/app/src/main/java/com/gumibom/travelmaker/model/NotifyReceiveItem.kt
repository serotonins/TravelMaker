package com.gumibom.travelmaker.model

data class NotifyReceiveItem (
        val requesterId: String,
        val requesterNickname: String,
        val requesterImage: Int,
        val requesterTrust: Int,
        val groupId: String,
        val description: String
)