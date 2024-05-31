package com.ssafy.gumibom.domain.pamphlet.entity;

import com.ssafy.gumibom.domain.record.entity.PersonalRecord;
import com.ssafy.gumibom.domain.record.entity.Record;
import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.global.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue(value = "P")
public class PersonalPamphlet extends Pamphlet {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Convert(converter = StringListConverter.class)
    private List<String> categories = new ArrayList<>();

    @OneToMany(mappedBy = "personalPamphlet", cascade = CascadeType.ALL)
    private List<PersonalRecord> personalRecords = new ArrayList<>();

    private Boolean isFinish;

    private String repreImgUrl;

    // 연관관계 편의 메서드
    // 양방향 연결일 때 편하게 메서드로 세팅

    public void setUser(User user) {
        this.user = user;
        user.getPersonalPamphlets().add(this);
    }

    @Override
    public void addRecord(Record record) {
        this.personalRecords.add((PersonalRecord) record);
    }

    @Override
    public void removeRecord(Record record) {
        this.personalRecords.remove((PersonalRecord) record);
    }

    // 팜플렛 제목 및 기본 정보를 세팅하는 함수
    public void setPamphlet(String title, List<String> categories, String repreImgUrl) {
        this.title = title;
        this.createTime = LocalDateTime.now();
        this.love = 0;
        this.categories = categories;
        this.isFinish = false;
        this.repreImgUrl = repreImgUrl;
    }

    // 개인 팜플렛을 종료시키는 함수 -> 변경 감지(dirty-checking)를 이용해 엔티티 수정
    public void finishPamphlet() {
        this.isFinish = true;
    }


    // 생성 메서드
    // 도메인 모델 패턴

    public static PersonalPamphlet createPersonalPamphlet(User user,
                                                          String title,
                                                          List<String> categories,
                                                          String repreImgUrl) {
        PersonalPamphlet pPamphlet = new PersonalPamphlet();
        pPamphlet.setPamphlet(title, categories, repreImgUrl);
        pPamphlet.setUser(user);
//        for(String category: categories) {
//            pPamphlet.categories.add(category);
//        }

        return pPamphlet;
    }
}
