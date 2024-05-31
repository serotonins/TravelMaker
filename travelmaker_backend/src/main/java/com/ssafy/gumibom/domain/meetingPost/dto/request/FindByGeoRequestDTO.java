package com.ssafy.gumibom.domain.meetingPost.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FindByGeoRequestDTO {

    private Double latitude;
    private Double longitude;
    private Double radius;
    private List<String> categories;

    public FindByGeoRequestDTO() {
        radius = 3.0;
        categories = new ArrayList<>(Arrays.asList("taste", "healing", "culture", "active", "picture", "nature", "shopping", "rest"));
    }
}
