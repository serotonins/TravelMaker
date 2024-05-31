package com.ssafy.gumibom.domain.meetingPost.dto.response;

import lombok.*;

import java.util.List;

@Getter
public class ShowAllJoinRequestResponseDto {

    List<ReceivedRequestResponseDto> receivedRequests;
    List<SentRequestResponseDto> sentRequests;

    public ShowAllJoinRequestResponseDto(
            List<ReceivedRequestResponseDto> receivedRequests,
            List<SentRequestResponseDto> sentRequests) {

        this.receivedRequests = receivedRequests;
        this.sentRequests = sentRequests;
    }
}
