package com.ssafy.gumibom.domain.user.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRequestDto {
    private String phone;
    private String certificationNumber;
}
