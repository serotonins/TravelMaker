package com.ssafy.gumibom.domain.record.dto.request;

import com.ssafy.gumibom.global.common.Emoji;
import com.ssafy.gumibom.global.util.EmojiConverter;
import jakarta.persistence.Convert;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonalRecordRequestDto {

    private Long pamphletId;
    private Long recordId;

    private String title;
    private String text;

    @Convert(converter = EmojiConverter.class)
    private Emoji emoji;
}
