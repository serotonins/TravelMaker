package com.ssafy.gumibom.domain.user.sms;

import com.ssafy.gumibom.global.base.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SmsResponseDto extends BaseResponseDto {
    public SmsResponseDto(Boolean isSuccess, String message) {
        super(isSuccess, message);
    }

    @Override
    public Boolean getIsSuccess() {
        return super.getIsSuccess();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
