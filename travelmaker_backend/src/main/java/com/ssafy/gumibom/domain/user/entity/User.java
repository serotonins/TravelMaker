package com.ssafy.gumibom.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.gumibom.domain.meeting.entity.MeetingMember;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingApplier;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingPost;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingRequest;
import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    @Email
    private String email;

    @NotEmpty
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender; // Gender는 enum 타입

    private String birth;

    @NotEmpty
    @Size(min = 10, max = 15)
    private String phone;

    @Lob
    @NotEmpty
    private String profileImgURL;

    private Double trust;

    @NotEmpty
    private String town;

    @Lob
    private String fcmtoken;

    private String nation;

    @ElementCollection
    private List<String> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonalPamphlet> personalPamphlets;

    @JsonIgnore
    @OneToMany(mappedBy = "requestor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MeetingRequest> sentRequests = new ArrayList<>(); // 요청자로서 보낸 모임 요청들

    @JsonIgnore
    @OneToMany(mappedBy = "acceptor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MeetingRequest> receivedRequests = new ArrayList<>(); // 방장으로서 받은 모임 요청들


    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingApplier> meetingAppliers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MeetingMember> meetingMembers = new ArrayList<>();

//    // Gender enum 타입 정의
//    public enum Gender {
//        MALE, FEMALE
//    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    // Init DB를 위한 테스트용 생성자
//    public User(String username, String password, String nickname, String phone) {
//        this.username = username;
//        this.password = password;
//        this.nickname = nickname;
//        this.phone = phone;
//    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Role을 GrantedAuthority로 변환
        GrantedAuthority authority = new SimpleGrantedAuthority(role.name());

        // 변환된 GrantedAuthority를 담은 컬렉션 반환
        return Collections.singletonList(authority);
    }

    public User setProfileImgURL(String profileImgURL) {
        this.profileImgURL = profileImgURL;
        return this;
    }

    public void updateFCM(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public Boolean isAccountNonLocked() {
        return true;
    }

    public Boolean isCredentialsNonExpired() {
        return true;
    }

    public Boolean isEnabled() {
        return true;
    }

    public void setMeetingApplier(MeetingApplier meetingApplier) {
        this.meetingAppliers.add(meetingApplier);
        meetingApplier.setUser(this);
    }


    public void setMeetingMember(MeetingMember meetingMember) {
        this.meetingMembers.add(meetingMember);
        meetingMember.setUser(this);
    }

    public void setPassword(String password) {
        this.password = password;
    }


//    @Builder
//    public User(SignupRequestDto signupRequestDto){
//        this.username = signupRequestDto.getUsername();
//        this.password = signupRequestDto.getPassword();
//        this.email = signupRequestDto.getEmail();
//        this.gender = signupRequestDto.getGender();
//        this.phone = signupRequestDto.getPhone();
//        this.nation = signupRequestDto.getNation();
//        this.categories = signupRequestDto.getCategories();
//        this.imgURL = signupRequestDto.getProfileImgURL();
//    }
//
//    //token 생성시 사용될 생성자
//    @Builder
//    public User(String username, String password, Role role) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }

//    public User(String loginId, String password, String nickname, String birth, String phone, boolean gender, List<Category> categories, Nation nation) {
//    }
//
//    public User(String subject, String password, Collection<? extends GrantedAuthority> authorities) {
//    }
//
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


//    @JsonIgnore
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<MeetingPost> meetingPosts;

}
