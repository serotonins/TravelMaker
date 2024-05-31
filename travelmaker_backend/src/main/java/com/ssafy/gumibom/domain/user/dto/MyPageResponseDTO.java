package com.ssafy.gumibom.domain.user.dto;

import com.ssafy.gumibom.domain.user.entity.Gender;
import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.global.common.Nation;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponseDTO {

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

    public static MyPageResponseDTO createMyPageResponseDTO(User user){
        return MyPageResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .profileImgURL(user.getProfileImgURL())
                .trust(user.getTrust())
                .town(user.getTown())
                .nation(user.getNation())
                .categories(user.getCategories())
                .build();
    }
}
