package com.ssafy.gumibom.domain.meeting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.gumibom.domain.meetingPost.dto.DetailMeetingPostResForMeetingDto;
import com.ssafy.gumibom.domain.pamphlet.entity.MeetingPamphlet;
import com.ssafy.gumibom.global.common.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Long id;

    @ElementCollection
    private List<String> categories;

    private String title;
    private String imgUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ColumnDefault("false")
    private Boolean isFinish; // 모임 진행 중인지, 모임 완료되었는지

    @Embedded
    private Position position;

    @JsonIgnore
    @OneToMany(mappedBy = "meeting")
    private List<MeetingMember> meetingMembers= new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "pamphlet_id")
    private MeetingPamphlet meetingPamphlet;


    public Meeting(DetailMeetingPostResForMeetingDto detailMeetingPostResForMeetingDto) {
        this.title = detailMeetingPostResForMeetingDto.getTitle();
        this.startDate = detailMeetingPostResForMeetingDto.getStartDate();
        this.endDate = detailMeetingPostResForMeetingDto.getEndDate();
        this.imgUrl = detailMeetingPostResForMeetingDto.getImgUrl();
        this.categories = detailMeetingPostResForMeetingDto.getCategories();
//        this.meetingMembers = detailMeetingPostResForMeetingDto.getMembers();
        this.isFinish = true;
        this.position = detailMeetingPostResForMeetingDto.getPosition();
    }

    public void finishMeeting(){
        this.isFinish = true;
    }
}
