package com.ssafy.gumibom.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType; // jwt 인증 타입. (Bearer)
    private String accessToken;
    private String refreshToken;
}
