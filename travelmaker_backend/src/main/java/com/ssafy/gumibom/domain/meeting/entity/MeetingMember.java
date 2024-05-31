package com.ssafy.gumibom.domain.meeting.entity;


import com.ssafy.gumibom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MeetingMember {

    @Id @GeneratedValue
    @Column(name = "meeting_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isNative;
    private Boolean isHead;

    public void setUser(User user) {
        this.user = user;
    }

    public void setMeeting(Meeting meeting){
        this.meeting = meeting;
    }

    public void setIsNative(Boolean isNative){
        this.isNative = isNative;
    }

    public void setIsHead(Boolean isHead){
        this.isHead = isHead;
    }

}
