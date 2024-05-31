package com.ssafy.gumibom.global.base;

import lombok.Getter;

@Getter
public class BooleanResponseDto extends BaseResponseDto {
    private final Boolean booleanValue;

    public BooleanResponseDto(Boolean isSuccess, String message, boolean booleanValue) {
        super(isSuccess, message);
        this.booleanValue = booleanValue;
    }
}
