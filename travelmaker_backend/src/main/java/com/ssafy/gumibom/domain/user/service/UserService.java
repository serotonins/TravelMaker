package com.ssafy.gumibom.domain.user.service;

import com.ssafy.gumibom.domain.user.dto.*;
import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.domain.user.repository.UserRepository;
import com.ssafy.gumibom.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final BCryptPasswordEncoder  encoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인
    @Transactional
    public JwtToken login(String loginId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // jwt 토큰 생성
        JwtToken token = jwtTokenProvider.generateToken(authentication);

        return token;
    }


    //회원가입
    @Transactional
    public Boolean signup(SignupRequestDto requestDto, MultipartFile image) throws IOException {

        String imgUrl = "";
        if (image != null) imgUrl = s3Service.uploadS3(image, "images");

//        boolean check = checkPhoneNumExists(requestDto.getPhone());
//        if (check) throw new IllegalArgumentException("이미 가입된 전화번호입니다.");

        String encPwd = encoder.encode(requestDto.getPassword());
        userRepository.save(requestDto.toEntity(encPwd).setProfileImgURL(imgUrl));

        User user = userRepository.findByUsername(requestDto.getUsername());

        if (user != null) {
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);

//        return user.getId();
    }

    // fcm 토큰 저장
    @Transactional
    public Boolean updateFCMById(AccountModifyRequestDTO requestDTO) {

        User user = userRepository.findByUsername(requestDTO.getUserLoginId());

        if (user == null) throw new NoSuchElementException();

        user.updateFCM(requestDTO.getModifyField());

        return true;
    }


    // 전화번호 중복 가입 체크
    public Boolean checkPhoneNumExists(String phoneNum) {
        return userRepository.existUsersByPhoneNum(phoneNum);
    }

    // 닉네임 중복 체크
    public Boolean checkNickNameExists(String nickName) {
        return userRepository.existUsersByNickName(nickName);
    }

    // 로그인 아이디 중복 체크
    public Boolean checkLoginIDExists(String loginID) {
        return userRepository.existUsersByLoginID(loginID);
    }

    public Optional<User> findLoginIDByPhoneNum(String phoneNum) {

//        boolean check = checkPhoneNumExists(phoneNum);
//        if (!check) throw new IllegalArgumentException("가입되지 않은 전화번호입니다.");
        return userRepository.findByPhoneNum(phoneNum);
    }

    @Transactional
    public Boolean changePassword(PasswordChangeRequestDto requestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUserName = authentication.getName();

        User user = userRepository.findByUsername(requestDto.getUserLoginId());

        if (user != null) {
            user.setPassword(encoder.encode(requestDto.getNewPassword()));
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    // mypage
    @Transactional
    public MyPageResponseDTO inquiryMyPage(String userLoginId) {
//        return userRepository.findByUsername(userLoginId);
        User user = userRepository.findByUsername(userLoginId);
        return MyPageResponseDTO.createMyPageResponseDTO(user);
    }

    /*


    private void validateDuplicateUser(User user){
        User findUser = userRepository.findByUsername(user.getUsername());
        if(findUser != null){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    */

}
