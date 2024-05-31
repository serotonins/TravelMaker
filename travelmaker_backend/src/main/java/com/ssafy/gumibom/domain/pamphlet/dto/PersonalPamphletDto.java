package com.ssafy.gumibom.domain.pamphlet.dto;

import ch.qos.logback.core.testUtil.StringListAppender;
import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
import com.ssafy.gumibom.domain.record.dto.PersonalRecordDto;
import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.global.util.StringListConverter;
import jakarta.persistence.Convert;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@EqualsAndHashCode(of = "pamphletId")
public class PersonalPamphletDto {

    private Long pamphletId;
    private String nickname;
    private String title;
    private Integer love;
    private LocalDateTime createTime;
//    private List<PersonalRecordDto> records;
    private Boolean isFinish;
    private String repreImgUrl;

    @Convert(converter = StringListConverter.class)
    private List<String> categories;

    public PersonalPamphletDto(PersonalPamphlet pPamphlet) {
        this.pamphletId = pPamphlet.getId();
        this.nickname = pPamphlet.getUser().getNickname();
        this.title = pPamphlet.getTitle();
        this.love = pPamphlet.getLove();
        this.createTime = pPamphlet.getCreateTime();
//        this.records = pPamphlet.getPersonalRecords().stream()
//                .map(record -> new PersonalRecordDto(record))
//                .collect(Collectors.toList());
        this.categories = pPamphlet.getCategories();
        this.isFinish = pPamphlet.getIsFinish();
        this.repreImgUrl = pPamphlet.getRepreImgUrl();
    }

}
