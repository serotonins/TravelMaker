package com.ssafy.gumibom.domain.user.dto;

import com.ssafy.gumibom.domain.meeting.dto.MeetingMemberDto;
import com.ssafy.gumibom.domain.user.entity.Gender;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public class UserDto {
    private Long userId;
    private String username;
    @Email
    private String email;
    private String nickname;
    private Gender gender; // Gender는 enum 타입
    private String birth;
    @Size(min = 10, max = 15)
    private String phone;
    @Lob
    private String profileImgURL;
    private Double trust;
    private String town;
    private String nation;
    private List<String> categories;
    private List<MeetingMemberDto> members;
}
