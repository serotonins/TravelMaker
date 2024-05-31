package com.ssafy.gumibom.global.base;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StringResponseDto extends BaseResponseDto {

    private final String stringValue;

    public StringResponseDto(Boolean isSuccess, String message, String stringValue) {
        super(isSuccess, message);
        this.stringValue = stringValue;
    }
}
