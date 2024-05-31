package com.ssafy.gumibom.domain.record.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeletePersonalRecordRequestDto {

    @NotBlank
    private Long pamphletId;

    @NotBlank
    private Long recordId;
}
