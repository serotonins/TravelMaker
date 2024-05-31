package com.ssafy.gumibom.domain.pamphlet.dto.response;

import com.ssafy.gumibom.global.base.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MakePersonalPamphletResponseDto extends BaseResponseDto {

    private Long pamphletId;

    public MakePersonalPamphletResponseDto(Boolean isSuccess, String message, Long pamphletId) {
        super(isSuccess, message);
        this.pamphletId = pamphletId;
    }
}
