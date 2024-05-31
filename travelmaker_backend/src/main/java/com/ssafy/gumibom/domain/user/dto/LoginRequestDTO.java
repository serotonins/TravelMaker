package com.ssafy.gumibom.domain.user.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String loginId;
    private String password;
}
