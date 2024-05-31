package com.ssafy.gumibom.domain.pamphlet.dto.request;


import com.ssafy.gumibom.global.util.StringListConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MakePersonalPamphletRequestDto {

    @NotBlank
    private Long userId;

    @NotBlank
    private String title;

    @Convert(converter = StringListConverter.class)
    private List<String> categories;

}
