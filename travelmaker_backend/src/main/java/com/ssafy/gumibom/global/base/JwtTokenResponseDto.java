package com.ssafy.gumibom.global.base;

import com.ssafy.gumibom.domain.user.dto.JwtToken;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenResponseDto extends BaseResponseDto {
    private final JwtToken jwtToken;
    public JwtTokenResponseDto(Boolean isSuccess, String message, JwtToken jwtToken) {
        super(isSuccess, message);
        this.jwtToken = jwtToken;
    }
}
