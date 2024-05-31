package com.gumibom.travelmaker.data.dto.request

data class FcmGetNotifyListDTO(
    val receivedRequests: List<ReceivedRequest>,
    val sentRequests: List<SentRequest>
)