package com.ssafy.gumibom.domain.user.sms;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sms", description = "문자 인증 관련 api")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sms-certification")
public class SmsController {
    private final SmsService smsService;

    @Operation(summary = "인증번호 문자 발송 api")
    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody SmsRequestDto requestDto) throws Exception {
        try {
            smsService.sendSms(requestDto);
            String message = "문자 발송이 정상적으로 완료되었습니다";
            SmsResponseDto responseDto = new SmsResponseDto(true, message);
            log.info("string 문자 성공===================================");
            log.error("왜안떠 이거");
            return ResponseEntity.ok(responseDto);
        } catch (CustomExceptions.Exception e) {
            log.info("string 문자 에러" + e.getMessage());
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + e.getMessage()+"@@@@@@@@@@@@@@@");
            return handleApiException(e, HttpStatus.BAD_REQUEST);

        }
    }

    //인증번호 확인
    @Operation(summary = "인증번호 검증 api")
    @PostMapping("/confirm")
    public ResponseEntity<?> SmsVerification(@RequestBody SmsRequestDto requestDto) throws Exception{
        try {
            smsService.verifySms(requestDto);
            String message ="문자 인증이 성공했습니다.";
            SmsResponseDto responseDto = new SmsResponseDto(true, message);
            return ResponseEntity.ok(responseDto);
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<DefaultRes<String>> handleApiException(CustomExceptions.Exception e, HttpStatus status) {
        DefaultRes<String> response = DefaultRes.res(status.value(), e.getMessage());
        return new ResponseEntity<>(response, status);
    }
}
