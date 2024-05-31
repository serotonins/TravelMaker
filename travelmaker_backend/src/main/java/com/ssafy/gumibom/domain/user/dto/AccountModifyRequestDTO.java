package com.ssafy.gumibom.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountModifyRequestDTO {

    private String userLoginId;
    private String modifyField;
}
