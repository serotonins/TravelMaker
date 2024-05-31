package com.ssafy.gumibom.domain.meeting.dto;

import com.ssafy.gumibom.domain.meeting.entity.Meeting;
import com.ssafy.gumibom.domain.meeting.entity.MeetingMember;
import com.ssafy.gumibom.domain.user.dto.UserDto;
import com.ssafy.gumibom.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingMemberDto {
    private Long id;
    private Boolean isNative;
    private Boolean isHead;


//    public MeetingMemberDto(MeetingMember member){
//        this.id = member.getId();
//        this.isHead = member.getIsHead();
//        this.isNative = member.getIsNative();
//
//    }
}
