package com.ssafy.gumibom.domain.meetingPost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.gumibom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MeetingRequest {

    @Id
    @GeneratedValue
    @Column(name = "meeting_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_post_id")
    private MeetingPost meetingPost;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "acceptor_id")
    private User acceptor;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "requestor_id")
    private User requestor;

    private Boolean whetherGetResponse; // 승낙 혹은 거절 응답을 받았는지 안받았는지


    // 연관관계 편의 메서드
    public void setAcceptor(User acceptor) {
        this.acceptor = acceptor;
        acceptor.getReceivedRequests().add(this);
    }

    public void setRequestor(User requestor) {
        this.requestor = requestor;
        requestor.getSentRequests().add(this);
    }

    public void initWhetherGetResponse() {
        this.whetherGetResponse = false;
    }

    public void getResponse() {
        this.whetherGetResponse = true;
    }

    // 생성 메서드
    public static MeetingRequest createRequest(MeetingPost meetingPost, User requestor, User acceptor) {
        MeetingRequest request = new MeetingRequest();

        request.meetingPost = meetingPost;
        request.setAcceptor(acceptor);
        request.setRequestor(requestor);
        request.initWhetherGetResponse();

        return request;
    }
}
