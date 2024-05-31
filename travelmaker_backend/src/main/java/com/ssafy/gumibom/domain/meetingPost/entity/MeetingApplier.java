package com.ssafy.gumibom.domain.meetingPost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.gumibom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "meeting_applier", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "meeting_post_id"})
})
@Getter
@Setter
public class MeetingApplier {

    @Id
    @GeneratedValue
    @Column(name = "meeting_applier_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_post_id")
    private MeetingPost meetingPost;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isNative;
    private Boolean isHead;

}
