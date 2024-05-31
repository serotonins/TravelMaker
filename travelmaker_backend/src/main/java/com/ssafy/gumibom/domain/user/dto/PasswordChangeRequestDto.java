package com.ssafy.gumibom.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDto {
    private String userLoginId;
    private String newPassword;
}
