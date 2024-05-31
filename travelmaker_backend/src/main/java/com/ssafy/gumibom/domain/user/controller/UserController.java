package com.ssafy.gumibom.domain.user.controller;

import com.ssafy.gumibom.domain.user.dto.*;
import com.ssafy.gumibom.domain.user.service.JwtTokenProvider;
import com.ssafy.gumibom.domain.user.service.UserService;
import com.ssafy.gumibom.global.base.BaseResponseDto;
import com.ssafy.gumibom.global.base.BooleanResponseDto;
import com.ssafy.gumibom.global.base.JwtTokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Tag(name = "User", description = "회원 관련 api")
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<JwtToken> loginSuccess(@RequestBody LoginRequestDTO requestDTO) {
        JwtToken token = userService.login(requestDTO.getLoginId(), requestDTO.getPassword());
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "회원가입")
    @PostMapping(value = "/join", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> signup(@RequestPart SignupRequestDto requestDto, @RequestPart MultipartFile image) throws IOException {
        log.info("requestDto" + requestDto.getUsername());
        return ResponseEntity.ok(new BaseResponseDto(userService.signup(requestDto, image), "회원가입 성공!"));
    }

    // 전화번호 중복 체크
    @Operation(summary = "전화번호 중복 체크")
    @GetMapping("/join/check/phone-number-exists/{phonenum}")
    public ResponseEntity<?> checkPhoneNumDuplicate(@PathVariable String phonenum) {
        return ResponseEntity.ok(new BooleanResponseDto(true, "전화번호 중복 체크 성공", userService.checkPhoneNumExists(phonenum)));
    }

    // 닉네임 중복 체크
    @Operation(summary = "닉네임 중복 체크")
    @GetMapping("/join/check/nickname-exists/{nickname}")
    public ResponseEntity<?> checkNickNameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(new BooleanResponseDto(true, "닉네임 중복 체크 성공", userService.checkNickNameExists(nickname)));
    }

    // Login ID 중복 체크
    @Operation(summary = "아이디 중복 체크")
    @GetMapping("/join/check/id-exists/{loginID}")
    public ResponseEntity<?> checkLoginIDDuplicate(@PathVariable String loginID) {
        return ResponseEntity.ok(new BooleanResponseDto(true, "아이디 중복 체크 성공", userService.checkLoginIDExists(loginID)));
    }

    @Operation(summary = "전화 번호로 아이디 찾기", description = "제공된 전화번호에 해당하는 사용자의 로그인 아이디를 찾습니다.")
    @GetMapping("/user/find-login-id/{phoneNum}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User login ID found successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "204", description = "No content, user not found for the provided phone number", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid phone number format", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> findLoginID(@PathVariable String phoneNum) {
        return userService.findLoginIDByPhoneNum(phoneNum)
                .map(user -> ResponseEntity.ok().body(user.getUsername()))
                .orElse(ResponseEntity.noContent().build()); // 전화번호에 해당하는 사용자가 없는 경우 204 No Content 반환
    }

    @Operation(summary = "비밀번호 변경(/join/check/id-exists/{loginID} 을 거쳐 true 떠서 유효한 사용자인지 확인되고 나면 \n비밀번호 변경하는 창이 활성화되어 오는 곳입니다...")
    @PostMapping("/user/change-password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user or bad request", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, login required", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequestDto request) {
        boolean success = userService.changePassword(request);

        if (success) {
            return ResponseEntity.ok().body("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid user");
        }
    }

    // 회원 탈퇴 API
    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or user not found", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, login required", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/user/withdrawal")
    public ResponseEntity<?> deleteUser(Principal principal) {
        String username = principal.getName(); // 현재 인증된 사용자의 사용자명을 가져옵니다.
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().body("Password changed successfully");
    }


    @Operation(summary = "마이페이지")
    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(userService.inquiryMyPage(username));
    }

    @Operation(summary = "fcm 토큰 갱신")
    @PutMapping("/user/renewal-fcm-token")
    public ResponseEntity<?> renewalFCM(@RequestBody AccountModifyRequestDTO requestDTO) {
        return ResponseEntity.ok(new BooleanResponseDto(true, "fcm token 갱신 성공", userService.updateFCMById(requestDTO)));
    }

    @Operation(summary = "access token 만료 시 refresh token으로 토큰 갱신하기")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("X-Refresh-Token");

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        // 새로운 토큰 생성
        JwtToken newTokens = jwtTokenProvider.generateTokenFromRefreshToken(refreshToken);
        return ResponseEntity.ok(newTokens);
    }

}
