package com.ssafy.gumibom.domain.meetingPost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.gumibom.domain.meetingPost.dto.request.WriteMeetingPostRequestDTO;
import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.global.common.Position;
import com.ssafy.gumibom.global.util.StringListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_post_id")
    private Long id;

    //    @JsonIgnore
//    @OneToMany(mappedBy = "meeting_post")
//    @Convert(converter = StringListConverter.class)
    @ElementCollection
    private List<String> categories = new ArrayList<>();

    private String title;
    private String content;
    private LocalDateTime authDate;
    private Integer nativeMin;
    private Integer travelerMin;
    private Integer memberMax;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @ColumnDefault("false")
    private Boolean isFinish;
    @ColumnDefault("false")
    private Boolean isMeetingFinish;
    private LocalDateTime deadline;
    private String imgUrlMain;
    private String imgUrlSub;
    private String imgUrlThr;

    @JsonIgnore
    @OneToMany(mappedBy = "meetingPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingApplier> appliers = new ArrayList<>();

    @Embedded
    private Position position;

    public MeetingPost(String mainImgUrl, String subImgUrl, String thirdImgUrl, WriteMeetingPostRequestDTO requestDTO) {

        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
        this.authDate = requestDTO.getAuthDate();
        this.deadline = requestDTO.getDeadline();
        this.startDate = requestDTO.getStartDate();
        this.endDate = requestDTO.getEndDate();
        this.nativeMin = requestDTO.getNativeMin();
        this.travelerMin = requestDTO.getTravelerMin();
        this.memberMax = requestDTO.getMemberMax();
        this.isFinish = false;
        this.isMeetingFinish = false;
        this.imgUrlMain = mainImgUrl;
        this.imgUrlSub = subImgUrl;
        this.imgUrlThr = thirdImgUrl;
        this.categories = requestDTO.getCategories();
        this.position = requestDTO.getPosition();
    }

    public MeetingApplier addApplier(User user, Boolean isHead) {
        MeetingApplier meetingApplier = new MeetingApplier();
        meetingApplier.setUser(user);
        meetingApplier.setMeetingPost(this);

        user.setMeetingApplier(meetingApplier);
        if(position != null) meetingApplier.setIsNative(this.position.getTown().equals(user.getTown()));
        meetingApplier.setIsHead(isHead);
        appliers.add(meetingApplier);

        return meetingApplier;
    }

    public static MeetingPost createMeetingPost(
            String mainImgUrl,
            String subImgUrl,
            String thirdImgUrl,
            WriteMeetingPostRequestDTO requestDTO, User author) {

        MeetingPost meetingPost = new MeetingPost(mainImgUrl, subImgUrl, thirdImgUrl, requestDTO);

        meetingPost.addApplier(author, true);

        return meetingPost;
    }

    public MeetingPost updateMeetingPost(String mainImgUrl, String subImgUrl, String thirdImgUrl, WriteMeetingPostRequestDTO requestDTO) {

        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
        this.authDate = requestDTO.getAuthDate();

        this.deadline = requestDTO.getDeadline();
        // 기간 변경에 따른 status 변경
        if (this.deadline != null) {
            this.isFinish = this.deadline.isBefore(LocalDateTime.now());
        } else {
            this.isFinish = false;
        }

        this.startDate = requestDTO.getStartDate();
        this.endDate = requestDTO.getEndDate();
        this.nativeMin = requestDTO.getNativeMin();
        this.travelerMin = requestDTO.getTravelerMin();
        this.memberMax = requestDTO.getMemberMax();
        this.imgUrlMain = mainImgUrl;
        this.imgUrlSub = subImgUrl;
        this.imgUrlThr = thirdImgUrl;
        this.categories = requestDTO.getCategories();
        this.position = requestDTO.getPosition();

        return this;
    }

    public void updateMeetingPostStatus() {
        this.isFinish = true;
    }
    public void updateMeetingStatus() { this.isMeetingFinish = true; }

    public User getHead() {
        for(MeetingApplier applier: this.appliers) {
            if(applier.getIsHead()) return applier.getUser();
        }
        return null;
    }
}
