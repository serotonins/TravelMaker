package com.ssafy.gumibom.domain.meetingPost.dto.response;

import com.ssafy.gumibom.global.common.Position;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FindByGeoResponseDTO {

    private Long id;
    private Double latitude;
    private Double longitude;
//    private Position position;
}
