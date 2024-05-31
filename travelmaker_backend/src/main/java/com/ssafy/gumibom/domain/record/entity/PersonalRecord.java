package com.ssafy.gumibom.domain.record.entity;

import com.ssafy.gumibom.domain.pamphlet.entity.Pamphlet;
import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
import com.ssafy.gumibom.domain.pamphlet.repository.PersonalPamphletRepository;
import com.ssafy.gumibom.global.common.Emoji;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("P")
public class PersonalRecord extends Record {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pamphlet_id")
    private PersonalPamphlet personalPamphlet;

    public void updateRecord(String title, String imgUrl, String videoUrl, String videoThumbnailImg, String text, Emoji emoji) {
        this.setRecord(title, imgUrl, videoUrl, videoThumbnailImg, text, emoji);
    }

    // 연관관계 편의 메서드
    @Override
    protected void setPamphlet(Pamphlet pamphlet) {
        if(this.personalPamphlet!=null) {
            this.personalPamphlet.removeRecord(this);
        }
        this.personalPamphlet = (PersonalPamphlet) pamphlet;
        pamphlet.addRecord(this);
    }

    // 생성 메서드
    public static PersonalRecord createPersonalRecord(String title, String imgUrl, String videoUrl, String videoThumbnailUrl, String text, PersonalPamphlet pPamphlet, Emoji emoji) {
        PersonalRecord pRecord = new PersonalRecord();
        pRecord.setRecord(title, imgUrl, videoUrl, videoThumbnailUrl, text, emoji);
        pRecord.setPamphlet(pPamphlet);

        return pRecord;
    }
}
