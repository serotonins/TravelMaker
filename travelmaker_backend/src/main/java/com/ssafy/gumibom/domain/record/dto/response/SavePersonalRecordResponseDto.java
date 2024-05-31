package com.ssafy.gumibom.domain.record.dto.response;

import com.ssafy.gumibom.global.base.BaseResponseDto;
import lombok.Getter;

@Getter
public class SavePersonalRecordResponseDto extends BaseResponseDto {

    private Long recordId;

    public SavePersonalRecordResponseDto(Long recordId, Boolean isSuccess, String message) {
        super(isSuccess, message);
        this.recordId = recordId;
    }
}
