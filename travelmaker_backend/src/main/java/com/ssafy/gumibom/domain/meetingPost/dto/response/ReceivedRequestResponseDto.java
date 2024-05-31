package com.ssafy.gumibom.domain.meetingPost.dto.response;

import com.ssafy.gumibom.domain.meetingPost.entity.MeetingRequest;
import lombok.Getter;


@Getter
public class ReceivedRequestResponseDto {

    private Long requestId;

    private String acceptorName;

    private Long requestorId;
    private String requestorName;
    private String requestorImg;
    private Double requestorBelief;

    private Long meetingPostId;
    private String meetingPostTitle;

    public ReceivedRequestResponseDto(MeetingRequest request) {
        this.requestId = request.getId();

        this.acceptorName = request.getAcceptor().getNickname();

        this.requestorId = request.getRequestor().getId();
        this.requestorName = request.getRequestor().getNickname();
        this.requestorImg = request.getRequestor().getProfileImgURL();
        this.requestorBelief = request.getRequestor().getTrust();

        this.meetingPostId = request.getMeetingPost().getId();
        this.meetingPostTitle = request.getMeetingPost().getTitle();
    }
}
