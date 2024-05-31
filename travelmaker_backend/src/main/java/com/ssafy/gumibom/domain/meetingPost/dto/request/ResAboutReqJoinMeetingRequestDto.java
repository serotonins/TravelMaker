package com.ssafy.gumibom.domain.meetingPost.dto.request;

import lombok.Getter;

@Getter
public class ResAboutReqJoinMeetingRequestDto {

    private Long requestId;
    private Long meetingPostId;
    private Long requestorId;
}
