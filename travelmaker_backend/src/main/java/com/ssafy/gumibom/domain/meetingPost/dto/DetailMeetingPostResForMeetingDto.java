package com.ssafy.gumibom.domain.meetingPost.dto;

import com.ssafy.gumibom.domain.meetingPost.entity.MeetingApplier;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingPost;
import com.ssafy.gumibom.global.common.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DetailMeetingPostResForMeetingDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> categories;
    private String imgUrl;
    private Position position;
    private List<MeetingApplier> members;

    public DetailMeetingPostResForMeetingDto(MeetingPost meetingPost){
        this.id = meetingPost.getId();
        this.title = meetingPost.getTitle();
        this.startDate = meetingPost.getStartDate();
        this.endDate = meetingPost.getEndDate();
        this.categories = meetingPost.getCategories(); // 여기서는 String 리스트를 예상
        this.imgUrl = meetingPost.getImgUrlMain();
        this.position = meetingPost.getPosition();
        this.members = meetingPost.getAppliers();
    }
}
