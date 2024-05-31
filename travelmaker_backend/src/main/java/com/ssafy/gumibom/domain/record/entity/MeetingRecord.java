package com.ssafy.gumibom.domain.record.entity;

import com.ssafy.gumibom.domain.pamphlet.entity.MeetingPamphlet;
import com.ssafy.gumibom.domain.pamphlet.entity.Pamphlet;
import com.ssafy.gumibom.global.common.Emoji;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("M")
public class MeetingRecord extends Record {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pamphlet_id")
    private MeetingPamphlet meetingPamphlet;

    public MeetingRecord(String title, String text) {
    }

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn("user_id")
//    private User user; // 임시 데이터 타입

    // 생성 메서드
    public static MeetingRecord createMeetingRecord(Pamphlet pamphlet, String title, String text, Emoji emoji) {
        MeetingRecord meetingRecord = MeetingRecord.builder()
                .title(title)
                .text(text)
                .build();

        meetingRecord.setPamphlet(pamphlet);

        return meetingRecord;
    }

    // 연관관계 편의 메서드
    @Override
    public void setPamphlet(Pamphlet pamphlet) {
        if(this.meetingPamphlet!=null) {
            this.meetingPamphlet.removeRecord(this);
        }
        this.meetingPamphlet = (MeetingPamphlet) pamphlet;
        pamphlet.addRecord(this);
    }
}
