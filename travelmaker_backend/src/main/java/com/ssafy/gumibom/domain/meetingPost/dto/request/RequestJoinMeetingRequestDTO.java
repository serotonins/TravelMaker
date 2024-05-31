package com.ssafy.gumibom.domain.meetingPost.dto.request;

import lombok.Getter;

@Getter
public class RequestJoinMeetingRequestDTO {

    private Long requestorId;
    private Long meetingPostId;
}
