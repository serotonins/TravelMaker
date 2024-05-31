package com.ssafy.gumibom.domain.meetingPost.dto.response;

import com.ssafy.gumibom.domain.meetingPost.entity.MeetingRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SentRequestResponseDto {

    private Long requestId;

    private String requestorName;

    private String acceptorName;

    private String meetingPostTitle;
    private String meetingPostMainImg;
    private LocalDateTime meetingPostDeadline;

    public SentRequestResponseDto(MeetingRequest request) {
        this.requestId = request.getId();

        this.requestorName = request.getRequestor().getNickname();

        this.acceptorName = request.getAcceptor().getNickname();

        this.meetingPostTitle = request.getMeetingPost().getTitle();
        this.meetingPostMainImg = request.getMeetingPost().getImgUrlMain();
        this.meetingPostDeadline = request.getMeetingPost().getDeadline();
    }
}
