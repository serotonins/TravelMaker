package com.ssafy.gumibom.domain.meeting.dto;

import com.ssafy.gumibom.domain.meeting.entity.MeetingMember;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public class MeetingDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String imgUrl;
    private Boolean isFinish;
    private List<MeetingMemberDto> members;
}
