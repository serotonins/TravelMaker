package com.ssafy.gumibom.domain.user.dto;

import com.ssafy.gumibom.domain.user.entity.Gender;
import com.ssafy.gumibom.domain.user.entity.Role;
import com.ssafy.gumibom.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {


    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
    private String email;

    private String town;
    private Gender gender;
    private String birth;
    private String phone;
    //    private String profileImgURL;
    private String nation;
    private List<String> categories;

    public User toEntity(String encPwd) {
        return User.builder()
                .username(username)
                .password(encPwd)
                .nickname(nickname)
                .email(email)
                .town(town)
                .gender(gender)
                .birth(birth)
                .phone(phone)
                .nation(nation)
                .categories(categories)
                .role(Role.USER)
                .build();
    }

}
