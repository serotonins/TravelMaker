package com.ssafy.gumibom.domain.meetingPost.dto.request;

import com.ssafy.gumibom.global.common.Position;
import com.ssafy.gumibom.global.util.StringListConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WriteMeetingPostRequestDTO {

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

//    @Length(min = 8, max = 14, message = "비밀번호는 8자 이상, 14자 이하로 입력해주세요.")

    @NotEmpty(message = "user id가 입력되지 않았습니다.")
    private String username;
    private String content;

    private LocalDateTime authDate;
    private Integer nativeMin; // (현지 최소)
    private Integer travelerMin; // (여행 최소)
    private Integer memberMax;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Position position;
    private LocalDateTime deadline;

    @NotEmpty(message = "카테고리를 하나 이상 선택해주세요!")
    @Convert(converter = StringListConverter.class)
    private List<String> categories;
}
