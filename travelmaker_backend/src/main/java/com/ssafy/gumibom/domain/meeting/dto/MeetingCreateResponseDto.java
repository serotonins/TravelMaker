package com.ssafy.gumibom.domain.meeting.dto;

import com.ssafy.gumibom.global.base.BaseResponseDto;
import lombok.*;



@Getter
public class MeetingCreateResponseDto extends BaseResponseDto {
    public MeetingCreateResponseDto(Boolean isSuccess, String message) {
        super(isSuccess, message);
    }

    @Override
    public Boolean getIsSuccess() {
        return super.getIsSuccess();
    }

}
